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
package com.efangtec.workflow.engine.parser.impl;

import com.efangtec.workflow.engine.model.JoinModel;
import com.efangtec.workflow.engine.model.NodeModel;
import com.efangtec.workflow.engine.parser.AbstractNodeParser;

/**
 * 合并节点解析类
 * @author yuqs
 * @since 1.0
 */
public class JoinParser extends AbstractNodeParser {
	/**
	 * 产生JoinModel模型对象
	 */
	protected NodeModel newModel() {
		return new JoinModel();
	}
}