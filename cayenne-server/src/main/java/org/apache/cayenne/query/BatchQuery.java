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
name|DbAttribute
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
name|DbEntity
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
name|EntityResolver
import|;
end_import

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
name|List
import|;
end_import

begin_comment
comment|/**  * BatchQuery and its descendants allow to group similar data for the batch  * database modifications, including inserts, updates and deletes. Single  * BatchQuery corresponds to a parameterized PreparedStatement and a matrix of  * values.  *   */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|BatchQuery
implements|implements
name|Query
block|{
comment|/**      * @since 1.2      */
specifier|protected
name|DbEntity
name|dbEntity
decl_stmt|;
specifier|protected
name|String
name|name
decl_stmt|;
comment|/**      * @since 3.1      */
specifier|protected
name|DataMap
name|dataMap
decl_stmt|;
comment|/**      * @since 4.0      */
specifier|protected
name|List
argument_list|<
name|BatchQueryRow
argument_list|>
name|rows
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|DbAttribute
argument_list|>
name|dbAttributes
decl_stmt|;
comment|/**      * @since 4.0      */
specifier|public
name|BatchQuery
parameter_list|(
name|DbEntity
name|dbEntity
parameter_list|,
name|List
argument_list|<
name|DbAttribute
argument_list|>
name|dbAttributes
parameter_list|,
name|int
name|batchCapacity
parameter_list|)
block|{
name|this
operator|.
name|dbEntity
operator|=
name|dbEntity
expr_stmt|;
name|this
operator|.
name|rows
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|batchCapacity
argument_list|)
expr_stmt|;
name|this
operator|.
name|dbAttributes
operator|=
name|dbAttributes
expr_stmt|;
block|}
comment|/**      * @since 4.0      */
specifier|public
name|List
argument_list|<
name|BatchQueryRow
argument_list|>
name|getRows
parameter_list|()
block|{
return|return
name|rows
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
comment|/**      * @since 3.1      */
annotation|@
name|Override
specifier|public
name|DataMap
name|getDataMap
parameter_list|()
block|{
return|return
name|dataMap
return|;
block|}
comment|/**      * @since 3.1      */
specifier|public
name|void
name|setDataMap
parameter_list|(
name|DataMap
name|dataMap
parameter_list|)
block|{
name|this
operator|.
name|dataMap
operator|=
name|dataMap
expr_stmt|;
block|}
comment|/**      * Returns default select parameters.      *       * @since 1.2      */
annotation|@
name|Override
specifier|public
name|QueryMetadata
name|getMetaData
parameter_list|(
name|EntityResolver
name|resolver
parameter_list|)
block|{
return|return
operator|new
name|DefaultQueryMetadata
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|DbEntity
name|getDbEntity
parameter_list|()
block|{
return|return
name|dbEntity
return|;
block|}
block|}
return|;
block|}
comment|/**      * @since 1.2      */
annotation|@
name|Override
specifier|public
name|void
name|route
parameter_list|(
name|QueryRouter
name|router
parameter_list|,
name|EntityResolver
name|resolver
parameter_list|,
name|Query
name|substitutedQuery
parameter_list|)
block|{
name|router
operator|.
name|route
argument_list|(
name|router
operator|.
name|engineForDataMap
argument_list|(
name|dbEntity
operator|.
name|getDataMap
argument_list|()
argument_list|)
argument_list|,
name|this
argument_list|,
name|substitutedQuery
argument_list|)
expr_stmt|;
block|}
comment|/**      * Calls "batchAction" on the visitor.      *       * @since 1.2      */
annotation|@
name|Override
specifier|public
name|SQLAction
name|createSQLAction
parameter_list|(
name|SQLActionVisitor
name|visitor
parameter_list|)
block|{
return|return
name|visitor
operator|.
name|batchAction
argument_list|(
name|this
argument_list|)
return|;
block|}
comment|/**      * Returns true if the batch query uses optimistic locking.      *       * @since 1.1      */
specifier|public
name|boolean
name|isUsingOptimisticLocking
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
comment|/**      * Returns a DbEntity associated with this batch.      */
specifier|public
name|DbEntity
name|getDbEntity
parameter_list|()
block|{
return|return
name|dbEntity
return|;
block|}
comment|/**      * Returns a list of DbAttributes describing batch parameters.      */
specifier|public
name|List
argument_list|<
name|DbAttribute
argument_list|>
name|getDbAttributes
parameter_list|()
block|{
return|return
name|dbAttributes
return|;
block|}
comment|/**      * @deprecated since 4.0 use getRows().size().      */
annotation|@
name|Deprecated
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|rows
operator|.
name|size
argument_list|()
return|;
block|}
block|}
end_class

end_unit

