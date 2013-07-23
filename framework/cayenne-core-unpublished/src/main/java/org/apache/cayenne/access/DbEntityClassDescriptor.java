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
name|access
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
name|Iterator
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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|reflect
operator|.
name|ClassDescriptor
import|;
end_import

begin_comment
comment|/**  * A descriptor of a primary or secondary DbEntity for a given persistent class during  * commit.  *   * @since 3.0  */
end_comment

begin_class
class|class
name|DbEntityClassDescriptor
block|{
specifier|private
name|ClassDescriptor
name|classDescriptor
decl_stmt|;
specifier|private
name|List
argument_list|<
name|DbRelationship
argument_list|>
name|pathFromMaster
decl_stmt|;
specifier|private
name|DbEntity
name|dbEntity
decl_stmt|;
name|DbEntityClassDescriptor
parameter_list|(
name|ClassDescriptor
name|classDescriptor
parameter_list|)
block|{
name|this
operator|.
name|classDescriptor
operator|=
name|classDescriptor
expr_stmt|;
name|this
operator|.
name|dbEntity
operator|=
name|classDescriptor
operator|.
name|getEntity
argument_list|()
operator|.
name|getDbEntity
argument_list|()
expr_stmt|;
block|}
name|DbEntityClassDescriptor
parameter_list|(
name|ClassDescriptor
name|classDescriptor
parameter_list|,
name|ObjAttribute
name|masterAttribute
parameter_list|)
block|{
name|this
operator|.
name|classDescriptor
operator|=
name|classDescriptor
expr_stmt|;
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|masterAttribute
operator|.
name|getDbPathIterator
argument_list|()
decl_stmt|;
if|if
condition|(
name|masterAttribute
operator|.
name|isFlattened
argument_list|()
condition|)
block|{
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Object
name|object
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|object
operator|instanceof
name|DbRelationship
condition|)
block|{
if|if
condition|(
name|pathFromMaster
operator|==
literal|null
condition|)
block|{
name|pathFromMaster
operator|=
operator|new
name|ArrayList
argument_list|<
name|DbRelationship
argument_list|>
argument_list|(
literal|2
argument_list|)
expr_stmt|;
block|}
name|pathFromMaster
operator|.
name|add
argument_list|(
operator|(
name|DbRelationship
operator|)
name|object
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|object
operator|instanceof
name|DbAttribute
condition|)
block|{
name|this
operator|.
name|dbEntity
operator|=
operator|(
name|DbEntity
operator|)
operator|(
operator|(
name|DbAttribute
operator|)
name|object
operator|)
operator|.
name|getEntity
argument_list|()
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|dbEntity
operator|==
literal|null
condition|)
block|{
name|dbEntity
operator|=
name|classDescriptor
operator|.
name|getEntity
argument_list|()
operator|.
name|getDbEntity
argument_list|()
expr_stmt|;
block|}
block|}
name|boolean
name|isMaster
parameter_list|()
block|{
return|return
name|pathFromMaster
operator|==
literal|null
return|;
block|}
name|ClassDescriptor
name|getClassDescriptor
parameter_list|()
block|{
return|return
name|classDescriptor
return|;
block|}
name|List
argument_list|<
name|DbRelationship
argument_list|>
name|getPathFromMaster
parameter_list|()
block|{
return|return
name|pathFromMaster
return|;
block|}
name|DbEntity
name|getDbEntity
parameter_list|()
block|{
return|return
name|dbEntity
return|;
block|}
name|ObjEntity
name|getEntity
parameter_list|()
block|{
return|return
name|classDescriptor
operator|.
name|getEntity
argument_list|()
return|;
block|}
block|}
end_class

end_unit
