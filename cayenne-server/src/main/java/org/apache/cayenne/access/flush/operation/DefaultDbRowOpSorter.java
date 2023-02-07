begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|access
operator|.
name|flush
operator|.
name|operation
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
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
name|DataDomain
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
name|Provider
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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|map
operator|.
name|EntitySorter
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

begin_comment
comment|/**  * @since 4.2  */
end_comment

begin_class
specifier|public
class|class
name|DefaultDbRowOpSorter
implements|implements
name|DbRowOpSorter
block|{
specifier|protected
specifier|final
name|Provider
argument_list|<
name|DataDomain
argument_list|>
name|dataDomainProvider
decl_stmt|;
specifier|protected
specifier|volatile
name|Comparator
argument_list|<
name|DbRowOp
argument_list|>
name|comparator
decl_stmt|;
specifier|public
name|DefaultDbRowOpSorter
parameter_list|(
annotation|@
name|Inject
name|Provider
argument_list|<
name|DataDomain
argument_list|>
name|dataDomainProvider
parameter_list|)
block|{
name|this
operator|.
name|dataDomainProvider
operator|=
name|dataDomainProvider
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|DbRowOp
argument_list|>
name|sort
parameter_list|(
name|List
argument_list|<
name|DbRowOp
argument_list|>
name|dbRows
parameter_list|)
block|{
comment|// sort by id, operation type and entity relations
name|dbRows
operator|.
name|sort
argument_list|(
name|getComparator
argument_list|()
argument_list|)
expr_stmt|;
comment|// sort reflexively dependent objects
name|sortReflexive
argument_list|(
name|dbRows
argument_list|)
expr_stmt|;
return|return
name|dbRows
return|;
block|}
specifier|protected
name|void
name|sortReflexive
parameter_list|(
name|List
argument_list|<
name|DbRowOp
argument_list|>
name|sortedDbRows
parameter_list|)
block|{
name|DataDomain
name|dataDomain
init|=
name|dataDomainProvider
operator|.
name|get
argument_list|()
decl_stmt|;
name|EntitySorter
name|sorter
init|=
name|dataDomain
operator|.
name|getEntitySorter
argument_list|()
decl_stmt|;
name|EntityResolver
name|resolver
init|=
name|dataDomain
operator|.
name|getEntityResolver
argument_list|()
decl_stmt|;
name|DbEntity
name|lastEntity
init|=
literal|null
decl_stmt|;
name|int
name|start
init|=
literal|0
decl_stmt|;
name|int
name|idx
init|=
literal|0
decl_stmt|;
name|DbRowOp
name|lastRow
init|=
literal|null
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|sortedDbRows
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|DbRowOp
name|row
init|=
name|sortedDbRows
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|row
operator|.
name|getEntity
argument_list|()
operator|!=
name|lastEntity
condition|)
block|{
if|if
condition|(
name|lastEntity
operator|!=
literal|null
operator|&&
name|sorter
operator|.
name|isReflexive
argument_list|(
name|lastEntity
argument_list|)
condition|)
block|{
name|ObjEntity
name|objEntity
init|=
name|resolver
operator|.
name|getObjEntity
argument_list|(
name|lastRow
operator|.
name|getObject
argument_list|()
operator|.
name|getObjectId
argument_list|()
operator|.
name|getEntityName
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|DbRowOp
argument_list|>
name|reflexiveSublist
init|=
name|sortedDbRows
operator|.
name|subList
argument_list|(
name|start
argument_list|,
name|idx
argument_list|)
decl_stmt|;
name|sorter
operator|.
name|sortObjectsForEntity
argument_list|(
name|objEntity
argument_list|,
name|reflexiveSublist
argument_list|,
name|lastRow
operator|instanceof
name|DeleteDbRowOp
argument_list|)
expr_stmt|;
block|}
name|start
operator|=
name|idx
expr_stmt|;
name|lastEntity
operator|=
name|row
operator|.
name|getEntity
argument_list|()
expr_stmt|;
block|}
name|lastRow
operator|=
name|row
expr_stmt|;
name|idx
operator|++
expr_stmt|;
block|}
comment|// sort last chunk
if|if
condition|(
name|lastEntity
operator|!=
literal|null
operator|&&
name|sorter
operator|.
name|isReflexive
argument_list|(
name|lastEntity
argument_list|)
condition|)
block|{
name|ObjEntity
name|objEntity
init|=
name|resolver
operator|.
name|getObjEntity
argument_list|(
name|lastRow
operator|.
name|getObject
argument_list|()
operator|.
name|getObjectId
argument_list|()
operator|.
name|getEntityName
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|DbRowOp
argument_list|>
name|reflexiveSublist
init|=
name|sortedDbRows
operator|.
name|subList
argument_list|(
name|start
argument_list|,
name|idx
argument_list|)
decl_stmt|;
name|sorter
operator|.
name|sortObjectsForEntity
argument_list|(
name|objEntity
argument_list|,
name|reflexiveSublist
argument_list|,
name|lastRow
operator|instanceof
name|DeleteDbRowOp
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|Comparator
argument_list|<
name|DbRowOp
argument_list|>
name|getComparator
parameter_list|()
block|{
name|Comparator
argument_list|<
name|DbRowOp
argument_list|>
name|local
init|=
name|comparator
decl_stmt|;
if|if
condition|(
name|local
operator|==
literal|null
condition|)
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
name|local
operator|=
name|comparator
expr_stmt|;
if|if
condition|(
name|local
operator|==
literal|null
condition|)
block|{
name|local
operator|=
operator|new
name|DbRowComparator
argument_list|(
name|dataDomainProvider
operator|.
name|get
argument_list|()
operator|.
name|getEntitySorter
argument_list|()
argument_list|)
expr_stmt|;
name|comparator
operator|=
name|local
expr_stmt|;
block|}
block|}
block|}
return|return
name|local
return|;
block|}
specifier|protected
specifier|static
class|class
name|DbRowComparator
implements|implements
name|Comparator
argument_list|<
name|DbRowOp
argument_list|>
block|{
specifier|private
specifier|final
name|EntitySorter
name|entitySorter
decl_stmt|;
specifier|protected
name|DbRowComparator
parameter_list|(
name|EntitySorter
name|entitySorter
parameter_list|)
block|{
name|this
operator|.
name|entitySorter
operator|=
name|entitySorter
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
name|DbRowOp
name|left
parameter_list|,
name|DbRowOp
name|right
parameter_list|)
block|{
name|DbRowOpType
name|leftType
init|=
name|left
operator|.
name|accept
argument_list|(
name|DbRowTypeVisitor
operator|.
name|INSTANCE
argument_list|)
decl_stmt|;
name|DbRowOpType
name|rightType
init|=
name|right
operator|.
name|accept
argument_list|(
name|DbRowTypeVisitor
operator|.
name|INSTANCE
argument_list|)
decl_stmt|;
name|int
name|result
init|=
name|leftType
operator|.
name|compareTo
argument_list|(
name|rightType
argument_list|)
decl_stmt|;
comment|// 1. sort by op type
if|if
condition|(
name|result
operator|!=
literal|0
condition|)
block|{
return|return
name|result
return|;
block|}
comment|// 2. sort by entity relations
name|result
operator|=
name|entitySorter
operator|.
name|getDbEntityComparator
argument_list|()
operator|.
name|compare
argument_list|(
name|left
operator|.
name|getEntity
argument_list|()
argument_list|,
name|right
operator|.
name|getEntity
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|result
operator|!=
literal|0
condition|)
block|{
comment|// invert result for delete
return|return
name|leftType
operator|==
name|DbRowOpType
operator|.
name|DELETE
condition|?
operator|-
name|result
else|:
name|result
return|;
block|}
comment|// TODO: 3. sort updates by changed and null attributes to batch it better,
comment|//  need to check cost vs benefit though
return|return
name|result
return|;
block|}
block|}
specifier|protected
specifier|static
class|class
name|DbRowTypeVisitor
implements|implements
name|DbRowOpVisitor
argument_list|<
name|DbRowOpType
argument_list|>
block|{
specifier|private
specifier|static
specifier|final
name|DbRowTypeVisitor
name|INSTANCE
init|=
operator|new
name|DbRowTypeVisitor
argument_list|()
decl_stmt|;
annotation|@
name|Override
specifier|public
name|DbRowOpType
name|visitInsert
parameter_list|(
name|InsertDbRowOp
name|diffSnapshot
parameter_list|)
block|{
return|return
name|DbRowOpType
operator|.
name|INSERT
return|;
block|}
annotation|@
name|Override
specifier|public
name|DbRowOpType
name|visitUpdate
parameter_list|(
name|UpdateDbRowOp
name|diffSnapshot
parameter_list|)
block|{
return|return
name|DbRowOpType
operator|.
name|UPDATE
return|;
block|}
annotation|@
name|Override
specifier|public
name|DbRowOpType
name|visitDelete
parameter_list|(
name|DeleteDbRowOp
name|diffSnapshot
parameter_list|)
block|{
return|return
name|DbRowOpType
operator|.
name|DELETE
return|;
block|}
block|}
block|}
end_class

end_unit

