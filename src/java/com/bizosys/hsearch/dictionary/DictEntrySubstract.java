/*
* Copyright 2010 The Apache Software Foundation
*
* Licensed to the Apache Software Foundation (ASF) under one
* or more contributor license agreements.  See the NOTICE file
* distributed with this work for additional information
* regarding copyright ownership.  The ASF licenses this file
* to you under the Apache License, Version 2.0 (the
* "License"); you may not use this file except in compliance
* with the License.  You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.bizosys.hsearch.dictionary;

import com.bizosys.hsearch.common.IStorable;
import com.bizosys.hsearch.common.RecordScalar;
import com.bizosys.hsearch.hbase.NV;

/**
 * The sighting of a word in dictionary is 
 * decremented on subsequent document deletions containing the same word.
 * @author karan
 *
 */
public class DictEntrySubstract extends RecordScalar {
	
	/**
	 * A dictionary entry
	 */
	DictEntry entry;
	
	/**
	 * Default Constructor
	 * @param pk	Primary Key
	 * @param family	The Column Family
	 * @param name	The column name
	 * @param entry	Existing dictionary entry
	 */
	public DictEntrySubstract (IStorable pk, byte[] family, 
		byte[] name, DictEntry entry) {
		super(pk);
		super.kv = new NV(family, name, entry);
		this.entry = entry;
	}
	
	@Override
	public boolean merge(byte[] existingB) {
		if ( null != existingB) {
			DictEntry existingEntry = new DictEntry(existingB); 
			entry.fldFreq = existingEntry.fldFreq - entry.fldFreq;
			if ( entry.fldFreq <= 0)entry.fldFreq = 0;
			entry.addType(existingEntry.fldType);
			return true;
		}
		return false;
	}
}