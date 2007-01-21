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
name|jpa
operator|.
name|conf
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
name|jpa
operator|.
name|map
operator|.
name|JpaEntityMap
import|;
end_import

begin_comment
comment|/**  * Implements an algorithm for merging two JpaEntityMaps.  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|EntityMapMergeProcessor
block|{
specifier|protected
name|EntityMapLoaderContext
name|context
decl_stmt|;
specifier|public
name|EntityMapMergeProcessor
parameter_list|(
name|EntityMapLoaderContext
name|context
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
block|}
specifier|public
name|void
name|mergeOverride
parameter_list|(
name|JpaEntityMap
name|map
parameter_list|)
block|{
comment|// TODO: andrus, 5/3/2006 - implement merging.. May also combine with class
comment|// descriptor injection...
block|}
block|}
end_class

end_unit

