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
name|dbsync
operator|.
name|merge
operator|.
name|token
operator|.
name|model
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
name|dbsync
operator|.
name|merge
operator|.
name|context
operator|.
name|MergerContext
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
name|dbsync
operator|.
name|merge
operator|.
name|factory
operator|.
name|MergerTokenFactory
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
name|dbsync
operator|.
name|merge
operator|.
name|token
operator|.
name|MergerToken
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
name|DbJoin
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
name|DbRelationship
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
name|ObjAttribute
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
name|ObjEntity
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
comment|/**  * A {@link MergerToken} to remove a {@link DbAttribute} from a {@link DbEntity}.  *   */
end_comment

begin_class
specifier|public
class|class
name|DropColumnToModel
extends|extends
name|AbstractToModelToken
operator|.
name|EntityAndColumn
block|{
specifier|public
name|DropColumnToModel
parameter_list|(
name|DbEntity
name|entity
parameter_list|,
name|DbAttribute
name|column
parameter_list|)
block|{
name|super
argument_list|(
literal|"Drop Column"
argument_list|,
name|entity
argument_list|,
name|column
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|MergerToken
name|createReverse
parameter_list|(
name|MergerTokenFactory
name|factory
parameter_list|)
block|{
return|return
name|factory
operator|.
name|createAddColumnToDb
argument_list|(
name|getEntity
argument_list|()
argument_list|,
name|getColumn
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|execute
parameter_list|(
name|MergerContext
name|mergerContext
parameter_list|)
block|{
comment|// remove relationships mapped to column. duplicate List to prevent
comment|// ConcurrentModificationException
name|List
argument_list|<
name|DbRelationship
argument_list|>
name|dbRelationships
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|getEntity
argument_list|()
operator|.
name|getRelationships
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|DbRelationship
name|dbRelationship
range|:
name|dbRelationships
control|)
block|{
for|for
control|(
name|DbJoin
name|join
range|:
name|dbRelationship
operator|.
name|getJoins
argument_list|()
control|)
block|{
if|if
condition|(
name|join
operator|.
name|getSource
argument_list|()
operator|==
name|getColumn
argument_list|()
operator|||
name|join
operator|.
name|getTarget
argument_list|()
operator|==
name|getColumn
argument_list|()
condition|)
block|{
name|remove
argument_list|(
name|mergerContext
operator|.
name|getDelegate
argument_list|()
argument_list|,
name|dbRelationship
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// remove ObjAttribute mapped to same column
for|for
control|(
name|ObjEntity
name|objEntity
range|:
name|getEntity
argument_list|()
operator|.
name|mappedObjEntities
argument_list|()
control|)
block|{
name|ObjAttribute
name|objAttribute
init|=
name|objEntity
operator|.
name|getAttributeForDbAttribute
argument_list|(
name|getColumn
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|objAttribute
operator|!=
literal|null
condition|)
block|{
name|objEntity
operator|.
name|removeAttribute
argument_list|(
name|objAttribute
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|mergerContext
operator|.
name|getDelegate
argument_list|()
operator|.
name|objAttributeRemoved
argument_list|(
name|objAttribute
argument_list|)
expr_stmt|;
block|}
block|}
comment|// remove DbAttribute
name|getEntity
argument_list|()
operator|.
name|removeAttribute
argument_list|(
name|getColumn
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|mergerContext
operator|.
name|getDelegate
argument_list|()
operator|.
name|dbAttributeRemoved
argument_list|(
name|getColumn
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

