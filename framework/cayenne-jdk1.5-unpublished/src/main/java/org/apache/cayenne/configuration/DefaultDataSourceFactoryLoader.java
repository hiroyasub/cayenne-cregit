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
name|configuration
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
name|CayenneRuntimeException
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
name|di
operator|.
name|Inject
import|;
end_import

begin_comment
comment|/**  * A {@link DataSourceFactoryLoader} that loads factories explicitly configured in the  * {@link DataNodeDescriptor}. If the factory class is not explicitly configured, and the  * descriptor has a configuration resource attached to it, {@link XMLPoolingDataSourceFactory}  * is returned.  *   * @since 3.1  */
end_comment

begin_class
specifier|public
class|class
name|DefaultDataSourceFactoryLoader
implements|implements
name|DataSourceFactoryLoader
block|{
annotation|@
name|Inject
specifier|protected
name|AdhocObjectFactory
name|objectFactory
decl_stmt|;
specifier|public
name|DataSourceFactory
name|getDataSourceFactory
parameter_list|(
name|DataNodeDescriptor
name|nodeDescriptor
parameter_list|)
block|{
name|String
name|typeName
init|=
name|nodeDescriptor
operator|.
name|getDataSourceFactoryType
argument_list|()
decl_stmt|;
if|if
condition|(
name|typeName
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|nodeDescriptor
operator|.
name|getConfigurationSource
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|objectFactory
operator|.
name|newInstance
argument_list|(
name|DataSourceFactory
operator|.
name|class
argument_list|,
name|XMLPoolingDataSourceFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"DataNodeDescriptor '%s' has no explicit DataSourceFactoryType set and has no configuration resource"
argument_list|,
name|nodeDescriptor
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
block|}
return|return
name|objectFactory
operator|.
name|newInstance
argument_list|(
name|DataSourceFactory
operator|.
name|class
argument_list|,
name|typeName
argument_list|)
return|;
block|}
block|}
end_class

end_unit

