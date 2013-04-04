/*
 * Copyright 2005-8 Pi4 Technologies Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 * Change History:
 * 24 Jul 2008 : Initial version created by gary
 */
package org.savara.common.model.generator;

import org.savara.common.logging.FeedbackHandler;
import org.savara.common.resources.ResourceLocator;

/**
 * This interface represents a model generator.
 */
public interface ModelGenerator {
	
	/**
	 * This method determines whether the generator is appropriate for
	 * the specified source and target types.
	 * 
	 * @param source The source
	 * @param targetType The target type
	 * @return Whether the specified types are supported
	 */
	public boolean isSupported(Object source, String targetType);

	/**
	 * This method generates the contents of the target
	 * model(s) using information in the source model. A map
	 * is returned to cater for source models that generate multiple
	 * models.
	 * 
	 * @param source The source model
	 * @param handler The feedback handler
	 * @param locator The resource locator
	 * @return The target models, returned as a map of model name to model
	 */
	public java.util.Map<String,Object> generate(Object source, FeedbackHandler handler,
							ResourceLocator locator);
	
}
