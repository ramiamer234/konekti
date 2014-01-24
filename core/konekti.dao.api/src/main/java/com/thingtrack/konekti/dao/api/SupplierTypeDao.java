/*
 * Copyright 2011 Thingtrack, S.L.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.thingtrack.konekti.dao.api;

import com.thingtrack.konekti.dao.template.Dao;
import com.thingtrack.konekti.domain.SupplierGroup;
import com.thingtrack.konekti.domain.SupplierType;

/**
 * Supplier Group Data Access Layer
 * <p>
 * @author Thingtrack S.L.
 *
 */
public interface SupplierTypeDao extends Dao<SupplierType, Integer> {
	
	/**
	 * Supplier Group by its name
	 * @param name the unique name, not null
	 * @return {@link SupplierType}
	 * @throws Exception if the name is null
	 */
	public SupplierType getByName(String name) throws Exception;

}
