/*
 * Copyright (C) 2013 Stefan Moschinski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.devmond.shell;

import java.util.NoSuchElementException;

public interface CommandInput {

	/**
	 * @param option
	 *            the option for which the value is needed
	 * @return the value for the option or <code>null</code> if no value exists
	 */
	Object getValueForOption(Option option);

	/**
	 * @return <code>true</code> if the input has another argument
	 */
	boolean hasNextArgument();

	/**
	 * @return the value of the next argument
	 * @throws NoSuchElementException
	 *             if no next argument exists
	 */
	String nextArgument();

	boolean hasOption(Option option);

}
