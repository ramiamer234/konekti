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
package com.thingtrack.konekti.dao.impl.internal;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.thingtrack.konekti.dao.api.ClientDao;
import com.thingtrack.konekti.dao.template.JpaDao;
import com.thingtrack.konekti.domain.Client;
import com.thingtrack.konekti.domain.Organization;
import com.thingtrack.konekti.domain.User;

/**
 * @author Thingtrack S.L.
 *
 */
@Repository
public class ClientDaoImpl extends JpaDao<Client, Integer> implements ClientDao {
	@SuppressWarnings("unchecked")
	@Override
	public List<Client> getAll(Organization organization) throws Exception {
		StringBuffer queryString = new StringBuffer("SELECT p FROM " + getEntityName() + " p");
		queryString.append(" WHERE p.organization = :organization");

		Query query = (Query) getEntityManager().createQuery(queryString.toString());
		
		query.setParameter("organization", organization);
		
		return query.getResultList();
	}
	
	@Override
	public Client getByCode(Organization organization, String code) throws Exception {		
		Client client = (Client)getEntityManager()
		.createQuery("SELECT p FROM " + getEntityName() + " p WHERE p.organization = :organization AND p.code = :code")
				.setParameter("organization", organization)
				.setParameter("code", code).getSingleResult();

		return client;
		
	}
	
	@Override
	public Client getByUser(User user) throws Exception {		
		String queryString = "SELECT p";
		queryString += " FROM " + getEntityName() + " p";
		queryString += " WHERE p.user = :user";
		
		Query query = (Query) getEntityManager().createQuery(queryString)
		.setParameter("user", user);
		
		return (Client) query.getSingleResult();

	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Client> getAll(User user) throws Exception {
		StringBuffer queryString = new StringBuffer("SELECT p FROM " + getEntityName() + " p");

		if (user.getActiveOrganization() != null)
			queryString.append(" WHERE p.organization = :organization");

		Query query = (Query) getEntityManager().createQuery(queryString.toString());
		
		if (user.getActiveOrganization() != null)
			query.setParameter("organization", user.getActiveOrganization());
		
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Client> getAll(User user, boolean active) throws Exception {
		StringBuffer queryString = new StringBuffer("SELECT p FROM " + getEntityName() + " p");
		
		queryString.append( " WHERE p.active = :active");
		
		if (user.getActiveOrganization() != null)
			queryString.append( " AND p.organization = :organization");		
							
		Query query = (Query) getEntityManager().createQuery(queryString.toString());
		
		query.setParameter("active", active);
		
		if (user.getActiveOrganization() != null)
			query.setParameter("organization", user.getActiveOrganization());
						
		return query.getResultList();
	}
			
}
