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
name|project2
operator|.
name|validate
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
name|configuration
operator|.
name|DataChannelDescriptor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|util
operator|.
name|Util
import|;
end_import

begin_class
class|class
name|DataChannelValidator
block|{
name|void
name|validate
parameter_list|(
name|Object
name|object
parameter_list|,
name|ConfigurationValidationVisitor
name|configurationValidationVisitor
parameter_list|)
block|{
comment|// check for empty name
name|DataChannelDescriptor
name|domain
init|=
operator|(
name|DataChannelDescriptor
operator|)
name|object
decl_stmt|;
name|String
name|name
init|=
name|domain
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|Util
operator|.
name|isEmptyString
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|configurationValidationVisitor
operator|.
name|registerError
argument_list|(
literal|"Unnamed DataDomain."
argument_list|,
name|object
argument_list|)
expr_stmt|;
comment|// no more name assertions
return|return;
block|}
block|}
block|}
end_class

end_unit

