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
name|query
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|access
operator|.
name|MockQueryEngine
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
name|access
operator|.
name|QueryEngine
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
name|map
operator|.
name|DataMap
import|;
end_import

begin_class
specifier|public
class|class
name|MockQueryRouter
implements|implements
name|QueryRouter
block|{
specifier|protected
name|List
name|queries
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
specifier|public
name|void
name|reset
parameter_list|()
block|{
name|this
operator|.
name|queries
operator|=
operator|new
name|ArrayList
argument_list|()
expr_stmt|;
block|}
specifier|public
name|List
name|getQueries
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|queries
argument_list|)
return|;
block|}
specifier|public
name|int
name|getQueryCount
parameter_list|()
block|{
return|return
name|queries
operator|.
name|size
argument_list|()
return|;
block|}
specifier|public
name|void
name|route
parameter_list|(
name|QueryEngine
name|engine
parameter_list|,
name|Query
name|query
parameter_list|,
name|Query
name|substitutedQuery
parameter_list|)
block|{
name|queries
operator|.
name|add
argument_list|(
name|query
argument_list|)
expr_stmt|;
block|}
specifier|public
name|QueryEngine
name|engineForDataMap
parameter_list|(
name|DataMap
name|map
parameter_list|)
block|{
return|return
operator|new
name|MockQueryEngine
argument_list|()
return|;
block|}
block|}
end_class

end_unit

