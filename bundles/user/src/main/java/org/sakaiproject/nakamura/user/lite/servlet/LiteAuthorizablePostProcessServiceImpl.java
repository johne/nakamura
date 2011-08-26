/*
 * Licensed to the Sakai Foundation (SF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The SF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.sakaiproject.nakamura.user.lite.servlet;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.api.request.RequestParameterMap;
import org.apache.sling.servlets.post.Modification;
import org.apache.sling.servlets.post.ModificationType;
import org.apache.sling.servlets.post.SlingPostConstants;
import org.osgi.service.event.EventAdmin;
import org.sakaiproject.nakamura.api.lite.Repository;
import org.sakaiproject.nakamura.api.lite.Session;
import org.sakaiproject.nakamura.api.lite.authorizable.Authorizable;
import org.sakaiproject.nakamura.api.lite.authorizable.Group;
import org.sakaiproject.nakamura.api.user.LiteAuthorizablePostProcessService;
import org.sakaiproject.nakamura.api.user.LiteAuthorizablePostProcessor;
import org.sakaiproject.nakamura.api.user.UserConstants;
import org.sakaiproject.nakamura.user.lite.resource.LiteAuthorizableResourceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Component(immediate = true, metatype = true)
@Service(value = LiteAuthorizablePostProcessService.class)
public class LiteAuthorizablePostProcessServiceImpl implements
		LiteAuthorizablePostProcessService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(LiteAuthorizablePostProcessServiceImpl.class);

	@Reference
	protected Repository repository;

	@Reference
	protected EventAdmin eventAdmin;

	@Reference(target = "(default=true)")
	protected LiteAuthorizablePostProcessor defaultPostProcessor;

	private LiteAuthorizablePostProcessor[] orderedServices = new LiteAuthorizablePostProcessor[0];

	/**
	 * The set of services, to be ordered.
	 */
	private Map<LiteAuthorizablePostProcessor, Integer> serviceSet = Maps
			.newHashMap();

	public LiteAuthorizablePostProcessServiceImpl() {
	}

	public void process(SlingHttpServletRequest request,
			Authorizable authorizable, Session session, ModificationType change)
			throws Exception {
		process(request, authorizable, session, change,
				new HashMap<String, Object[]>());
	}

	public void process(SlingHttpServletRequest request,
			Authorizable authorizable, Session session,
			ModificationType change, Map<String, Object[]> parameters)
			throws Exception {
		// Set up the Modification argument.

		if (Boolean.valueOf(String.valueOf(authorizable
				.getProperty(UserConstants.PROP_BARE_AUTHORIZABLE)))) {
			return; // bare authorizables have no extra objects
		}

		final String pathPrefix = (authorizable instanceof Group) ? LiteAuthorizableResourceProvider.SYSTEM_USER_MANAGER_GROUP_PREFIX
				: LiteAuthorizableResourceProvider.SYSTEM_USER_MANAGER_USER_PREFIX;
		Modification modification = new Modification(change, pathPrefix
				+ authorizable.getId(), null);

		if (change != ModificationType.DELETE) {
			defaultPostProcessor.process(request, authorizable, session,
					modification, parameters);
		}
		for (LiteAuthorizablePostProcessor processor : orderedServices) {
			processor.process(request, authorizable, session, modification,
					parameters);
		}
		if (change == ModificationType.DELETE) {
			defaultPostProcessor.process(request, authorizable, session,
					modification, parameters);
		}
	}

	public void process(Authorizable authorizable, Session session,
			ModificationType change, SlingHttpServletRequest request)
			throws Exception {
		Map<String, Object[]> parameters = new HashMap<String, Object[]>();
		if (request != null) {
			RequestParameterMap originalParameters = request
					.getRequestParameterMap();
			for (String originalParameterName : originalParameters.keySet()) {
				if (originalParameterName
						.startsWith(SlingPostConstants.RP_PREFIX)
						// FIXME BL120 this is another hackaround for KERN-1584
						|| "sakai:group-joinable".equals(originalParameterName)
						|| "sakai:group-visible".equals(originalParameterName)
						|| "sakai:pages-visible".equals(originalParameterName))
				// end KERN-1584 hackaround
				{
					RequestParameter[] values = originalParameters
							.getValues(originalParameterName);
					String[] stringValues = new String[values.length];
					for (int i = 0; i < values.length; i++) {
						stringValues[i] = values[i].getString();
					}
					parameters.put(originalParameterName, stringValues);
				}
			}
		}
		process(request, authorizable, session, change, parameters);
	}

	public void addAuthorizablePostProcessor(
			LiteAuthorizablePostProcessor service, int order) {
		LOGGER.debug("About to add service " + service);
		synchronized (serviceSet) {
			serviceSet.put(service, order);
			createNewSortedList();
		}
	}

	public void removeAuthorizablePostProcessor(
			LiteAuthorizablePostProcessor service) {
		synchronized (serviceSet) {
			serviceSet.remove(service);
			createNewSortedList();
		}
	}

	private Comparator<LiteAuthorizablePostProcessor> getComparator(
			final Map<LiteAuthorizablePostProcessor, Integer> propertiesMap) {
		return new Comparator<LiteAuthorizablePostProcessor>() {
			public int compare(LiteAuthorizablePostProcessor o1,
					LiteAuthorizablePostProcessor o2) {
				int i1 = propertiesMap.get(o1);
				int i2 = propertiesMap.get(o2);

				return i1 - i2;
			}
		};
	}

	/**
	 * Generate a new sorted list.
	 */
	private void createNewSortedList() {
		List<LiteAuthorizablePostProcessor> serviceList = Lists
				.newArrayList(serviceSet.keySet());
		Collections.sort(serviceList, getComparator(serviceSet));
		saveArray(serviceList);
	}

	protected void saveArray(List<LiteAuthorizablePostProcessor> serviceList) {
		orderedServices = serviceList
				.toArray(new LiteAuthorizablePostProcessor[serviceList.size()]);
	}

}
