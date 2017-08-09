begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|template
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|exp
operator|.
name|ExpressionFactory
import|;
end_import

begin_comment
comment|/**  * Implements utility methods used inside Velocity templates when rendering  * SQLTemplates.  *   * @since 1.1  */
end_comment

begin_class
specifier|public
class|class
name|SQLTemplateRenderingUtils
block|{
comment|/** 	 * Returns the result of evaluation of expression with object. 	 */
specifier|public
name|Object
name|cayenneExp
parameter_list|(
name|Object
name|object
parameter_list|,
name|String
name|expression
parameter_list|)
block|{
return|return
name|ExpressionFactory
operator|.
name|exp
argument_list|(
name|expression
argument_list|)
operator|.
name|evaluate
argument_list|(
name|object
argument_list|)
return|;
block|}
block|}
end_class

end_unit

