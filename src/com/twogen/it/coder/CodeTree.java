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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 码树
 * @author 帮杰
 *
 * @param <T> 码符号类型
 */
@SuppressWarnings("serial")
public class CodeTree<T> extends HashMap<T,CodeTree<T>> {
	
	/**
	 * 所有编码路径
	 */
	private Paths<T> paths;
	
	/**
	 * 获取所有编码路径
	 * @return
	 */
	public Paths<T> paths() {
		if (paths == null) {
			Path<T> path = new Path<T>();
			paths = paths(path);
			Collections.sort(paths);
		}
		return paths;
	}
	
	/**
	 * 从给定路径开始，获取所有编码路径
	 * @param path
	 * @return
	 */
	private Paths<T> paths(Path<T> path) {
		Paths<T> paths = new Paths<T>();
		for (Map.Entry<T,CodeTree<T>> entry : entrySet()) {
			Path<T> nextPath = new Path<T>();
			nextPath.addAll(path);
			nextPath.add(entry.getKey());
			CodeTree<T> nextCodeTree = entry.getValue();
			if (nextCodeTree == null) {
				paths.add(nextPath);
			}else {
				paths.addAll(nextCodeTree.paths(nextPath));
			}
		}
		return paths;
	}

	/**
	 * 将所有编码路径以列表的方式给出
	 * @return
	 */
	public List<String> strings() {
		Paths<T> paths = paths();
		List<String> strings = new ArrayList<String>(paths.size());
		for (Path<T> path : paths) {
			strings.add(path.toString());
		}
		return strings;
	}

	/**
	 * 将所有编码路径以字符串的方式给出
	 * @return
	 */
	public String string() {
		StringBuilder sb = new StringBuilder();
		for (String path : strings()) {
			sb.append(path);
			sb.append("\n");
		}
		return sb.toString();
	}
	
}
	
