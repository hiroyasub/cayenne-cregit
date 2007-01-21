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
name|cache
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * @since 3.0  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|MapQueryCacheFactory
implements|implements
name|QueryCacheFactory
block|{
specifier|public
specifier|static
specifier|final
name|String
name|CACHE_SIZE_PROPERTY
init|=
literal|"cayenne.MapQueryCacheFactory.cacheSize"
decl_stmt|;
specifier|public
name|QueryCache
name|getQueryCache
parameter_list|(
name|Map
name|properties
parameter_list|)
block|{
if|if
condition|(
name|properties
operator|!=
literal|null
condition|)
block|{
name|Object
name|size
init|=
name|properties
operator|.
name|get
argument_list|(
name|CACHE_SIZE_PROPERTY
argument_list|)
decl_stmt|;
if|if
condition|(
name|size
operator|!=
literal|null
condition|)
block|{
try|try
block|{
return|return
operator|new
name|MapQueryCache
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|size
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
block|}
return|return
operator|new
name|MapQueryCache
argument_list|()
return|;
block|}
block|}
end_class

end_unit

