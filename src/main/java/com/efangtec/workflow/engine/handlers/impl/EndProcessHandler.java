/* Copyright 2013-2015 www.snakerflow.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.efangtec.workflow.engine.handlers.impl;

import com.efangtec.workflow.engine.SnakerEngine;
import com.efangtec.workflow.engine.SnakerException;
import com.efangtec.workflow.engine.access.QueryFilter;
import com.efangtec.workflow.engine.core.Execution;
import com.efangtec.workflow.engine.entity.Order;
import com.efangtec.workflow.engine.entity.Task;
import com.efangtec.workflow.engine.handlers.IHandler;
import com.efangtec.workflow.engine.helper.StringHelper;
import com.efangtec.workflow.engine.model.ProcessModel;
import com.efangtec.workflow.engine.model.SubProcessModel;
import com.efangtec.workflow.engine.entity.Process;


import java.util.List;

/**
 * 结束流程实例的处理器
 * @author yuqs
 * @since 1.0
 */
public class EndProcessHandler implements IHandler {
	/**
	 * 结束当前流程实例，如果存在父流程，则触发父流程继续执行
	 */
	public void handle(Execution execution) {
		SnakerEngine engine = execution.getEngine();
		Order order = execution.getOrder();
		List<Task> tasks = engine.query().getActiveTasks(new QueryFilter().setOrderId(order.getId()));
		for(Task task : tasks) {
			if(task.isMajor()) throw new SnakerException("存在未完成的主办任务,请确认.");
			engine.task().complete(task.getId(), SnakerEngine.AUTO);
		}
		/**
		 * 结束当前流程实例
		 */
		engine.order().complete(order.getId());
		
		/**
		 * 如果存在父流程，则重新构造Execution执行对象，交给父流程的SubProcessModel模型execute
		 */
		if(StringHelper.isNotEmpty(order.getParentId())) {
			Order parentOrder = engine.query().getOrder(order.getParentId());
			if(parentOrder == null) return;
			Process process = engine.process().getProcessById(parentOrder.getProcessId());
			ProcessModel pm = process.getModel();
			if(pm == null) return;
			SubProcessModel spm = (SubProcessModel)pm.getNode(order.getParentNodeName());
            Execution newExecution = new Execution(engine, process, parentOrder, execution.getArgs());
            newExecution.setChildOrderId(order.getId());
            newExecution.setTask(execution.getTask());
			spm.execute(newExecution);
			/**
			 * SubProcessModel执行结果的tasks合并到当前执行对象execution的tasks列表中
			 */
			execution.addTasks(newExecution.getTasks());
		}
	}
}
