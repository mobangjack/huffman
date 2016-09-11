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

import java.util.Collection;
import java.util.Random;

/**
 * 符号选择器
 * @author 帮杰
 *
 * @param <T> 码符号类型
 */
public class SymbolPicker<T> {
	
	/**
	 * 是否随机挑选
	 */
	private final boolean arbitrary;
	
	/**
	 * 符号集
	 */
	private final SymbolSet<T> symbolSet;

	public SymbolPicker(Collection<? extends T> symbolSet) {
		this.arbitrary = false;
		this.symbolSet = new SymbolSet<T>(symbolSet);
	}
	
	public SymbolPicker(boolean arbitrary, Collection<? extends T> symbolSet) {
		this.arbitrary = arbitrary;
		this.symbolSet = new SymbolSet<T>(symbolSet);
	}
	
	@SuppressWarnings("unchecked")
	public T next() {
		if (arbitrary) {
			Object[] symbols = symbolSet.toArray();
			Random random = new Random();
			int index = random.nextInt(symbols.length);
			T symbol = (T) symbols[index];
			symbolSet.remove(symbol);
			return symbol;
		}else {
			for (T symbol : symbolSet) {
				symbolSet.remove(symbol);
				return symbol;
			}
		}
		return null;
	}

	public boolean isArbitrary() {
		return arbitrary;
	}

	public SymbolSet<T> getSymbolSet() {
		return symbolSet;
	}
}
