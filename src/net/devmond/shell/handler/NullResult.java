/*
 * Copyright (C) 2013 Stefan Moschinski
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package net.devmond.shell.handler;

/**
 * Represents a result of a command that does not contain a value.
 */
class NullResult implements Result
{

	static Result nullResult()
	{
		return new NullResult();
	}

	@Override
	public String toString()
	{
		return null;
	}

	@Override
	public boolean isResultDisplayable()
	{
		return false;
	}

	private NullResult()
	{

	}
}
