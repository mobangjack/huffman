/**
 * Copyright (c) 2011-2015, Mobangjack 莫帮杰 (mobangjack@foxmail.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.twogen.it.coder;

/**
 * 编码器
 * @author 帮杰
 *
 * @param <T> 码符号类型
 */

public interface Coder<T>
{
	/**
	 * 编码入口函数
	 * @param probabilities 信源的概率分布
	 * @return 码树
	 */
	public CodeTree<T> code(double... probabilities);
}
