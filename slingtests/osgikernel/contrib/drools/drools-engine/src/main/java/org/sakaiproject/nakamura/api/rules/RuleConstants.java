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
package org.sakaiproject.nakamura.api.rules;

/**
 * Constants for Rules.
 */
public interface RuleConstants {
  /**
   * The OSGi service property that defines the name of the processor.
   */
  public static final String PROCESSOR_NAME = "processor.name";

  /**
   * The name of the property on the rule node that defines the processor name.
   */
  public static final String SAKAI_RULE_EXECUTION_PREPROCESSOR = "sakai:rule-execution-preprocessor";

}
