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
name|merge
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|dba
operator|.
name|DbAdapter
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

begin_comment
comment|/**  * All {@link MergerToken}s should be created from a {@link MergerFactory} obtained from  * {@link DbAdapter#mergerFactory()} so that the {@link DbAdapter} are able to provide  * {@link MergerToken} subclasses.  *   * @see DbAdapter#mergerFactory()  */
end_comment

begin_class
specifier|public
class|class
name|MergerFactory
block|{
specifier|public
name|MergerToken
name|createCreateTableToModel
parameter_list|(
name|DbEntity
name|entity
parameter_list|)
block|{
return|return
operator|new
name|CreateTableToModel
argument_list|(
name|entity
argument_list|)
return|;
block|}
specifier|public
name|MergerToken
name|createCreateTableToDb
parameter_list|(
name|DbEntity
name|entity
parameter_list|)
block|{
return|return
operator|new
name|CreateTableToDb
argument_list|(
name|entity
argument_list|)
return|;
block|}
specifier|public
name|MergerToken
name|createDropTableToModel
parameter_list|(
name|DbEntity
name|entity
parameter_list|)
block|{
return|return
operator|new
name|DropTableToModel
argument_list|(
name|entity
argument_list|)
return|;
block|}
specifier|public
name|MergerToken
name|createDropTableToDb
parameter_list|(
name|DbEntity
name|entity
parameter_list|)
block|{
return|return
operator|new
name|DropTableToDb
argument_list|(
name|entity
argument_list|)
return|;
block|}
specifier|public
name|MergerToken
name|createAddColumnToModel
parameter_list|(
name|DbEntity
name|entity
parameter_list|,
name|DbAttribute
name|column
parameter_list|)
block|{
return|return
operator|new
name|AddColumnToModel
argument_list|(
name|entity
argument_list|,
name|column
argument_list|)
return|;
block|}
specifier|public
name|MergerToken
name|createAddColumnToDb
parameter_list|(
name|DbEntity
name|entity
parameter_list|,
name|DbAttribute
name|column
parameter_list|)
block|{
return|return
operator|new
name|AddColumnToDb
argument_list|(
name|entity
argument_list|,
name|column
argument_list|)
return|;
block|}
specifier|public
name|MergerToken
name|createDropColumnToModel
parameter_list|(
name|DbEntity
name|entity
parameter_list|,
name|DbAttribute
name|column
parameter_list|)
block|{
return|return
operator|new
name|DropColumnToModel
argument_list|(
name|entity
argument_list|,
name|column
argument_list|)
return|;
block|}
specifier|public
name|MergerToken
name|createDropColumnToDb
parameter_list|(
name|DbEntity
name|entity
parameter_list|,
name|DbAttribute
name|column
parameter_list|)
block|{
return|return
operator|new
name|DropColumnToDb
argument_list|(
name|entity
argument_list|,
name|column
argument_list|)
return|;
block|}
specifier|public
name|MergerToken
name|createSetNotNullToModel
parameter_list|(
name|DbEntity
name|entity
parameter_list|,
name|DbAttribute
name|column
parameter_list|)
block|{
return|return
operator|new
name|SetNotNullToModel
argument_list|(
name|entity
argument_list|,
name|column
argument_list|)
return|;
block|}
specifier|public
name|MergerToken
name|createSetNotNullToDb
parameter_list|(
name|DbEntity
name|entity
parameter_list|,
name|DbAttribute
name|column
parameter_list|)
block|{
return|return
operator|new
name|SetNotNullToDb
argument_list|(
name|entity
argument_list|,
name|column
argument_list|)
return|;
block|}
specifier|public
name|MergerToken
name|createSetAllowNullToModel
parameter_list|(
name|DbEntity
name|entity
parameter_list|,
name|DbAttribute
name|column
parameter_list|)
block|{
return|return
operator|new
name|SetAllowNullToModel
argument_list|(
name|entity
argument_list|,
name|column
argument_list|)
return|;
block|}
specifier|public
name|MergerToken
name|createSetAllowNullToDb
parameter_list|(
name|DbEntity
name|entity
parameter_list|,
name|DbAttribute
name|column
parameter_list|)
block|{
return|return
operator|new
name|SetAllowNullToDb
argument_list|(
name|entity
argument_list|,
name|column
argument_list|)
return|;
block|}
specifier|public
name|MergerToken
name|createSetValueForNullToDb
parameter_list|(
name|DbEntity
name|entity
parameter_list|,
name|DbAttribute
name|column
parameter_list|,
name|ValueForNullProvider
name|valueForNullProvider
parameter_list|)
block|{
return|return
operator|new
name|SetValueForNullToDb
argument_list|(
name|entity
argument_list|,
name|column
argument_list|,
name|valueForNullProvider
argument_list|)
return|;
block|}
specifier|public
name|MergerToken
name|createSetColumnTypeToModel
parameter_list|(
name|DbEntity
name|entity
parameter_list|,
name|DbAttribute
name|columnOriginal
parameter_list|,
name|DbAttribute
name|columnNew
parameter_list|)
block|{
return|return
operator|new
name|SetColumnTypeToModel
argument_list|(
name|entity
argument_list|,
name|columnOriginal
argument_list|,
name|columnNew
argument_list|)
return|;
block|}
specifier|public
name|MergerToken
name|createSetColumnTypeToDb
parameter_list|(
name|DbEntity
name|entity
parameter_list|,
name|DbAttribute
name|columnOriginal
parameter_list|,
name|DbAttribute
name|columnNew
parameter_list|)
block|{
return|return
operator|new
name|SetColumnTypeToDb
argument_list|(
name|entity
argument_list|,
name|columnOriginal
argument_list|,
name|columnNew
argument_list|)
return|;
block|}
specifier|public
name|MergerToken
name|createAddRelationshipToDb
parameter_list|(
name|DbEntity
name|entity
parameter_list|,
name|DbRelationship
name|rel
parameter_list|)
block|{
return|return
operator|new
name|AddRelationshipToDb
argument_list|(
name|entity
argument_list|,
name|rel
argument_list|)
return|;
block|}
specifier|public
name|MergerToken
name|createAddRelationshipToModel
parameter_list|(
name|DbEntity
name|entity
parameter_list|,
name|DbRelationship
name|rel
parameter_list|)
block|{
return|return
operator|new
name|AddRelationshipToModel
argument_list|(
name|entity
argument_list|,
name|rel
argument_list|)
return|;
block|}
specifier|public
name|MergerToken
name|createDropRelationshipToDb
parameter_list|(
name|DbEntity
name|entity
parameter_list|,
name|DbRelationship
name|rel
parameter_list|)
block|{
return|return
operator|new
name|DropRelationshipToDb
argument_list|(
name|entity
argument_list|,
name|rel
argument_list|)
return|;
block|}
specifier|public
name|MergerToken
name|createDropRelationshipToModel
parameter_list|(
name|DbEntity
name|entity
parameter_list|,
name|DbRelationship
name|rel
parameter_list|)
block|{
return|return
operator|new
name|DropRelationshipToModel
argument_list|(
name|entity
argument_list|,
name|rel
argument_list|)
return|;
block|}
specifier|public
name|MergerToken
name|createSetPrimaryKeyToDb
parameter_list|(
name|DbEntity
name|entity
parameter_list|,
name|Collection
argument_list|<
name|DbAttribute
argument_list|>
name|primaryKeyOriginal
parameter_list|,
name|Collection
argument_list|<
name|DbAttribute
argument_list|>
name|primaryKeyNew
parameter_list|)
block|{
return|return
operator|new
name|SetPrimaryKeyToDb
argument_list|(
name|entity
argument_list|,
name|primaryKeyOriginal
argument_list|,
name|primaryKeyNew
argument_list|)
return|;
block|}
specifier|public
name|MergerToken
name|createSetPrimaryKeyToModel
parameter_list|(
name|DbEntity
name|entity
parameter_list|,
name|Collection
argument_list|<
name|DbAttribute
argument_list|>
name|primaryKeyOriginal
parameter_list|,
name|Collection
argument_list|<
name|DbAttribute
argument_list|>
name|primaryKeyNew
parameter_list|)
block|{
return|return
operator|new
name|SetPrimaryKeyToModel
argument_list|(
name|entity
argument_list|,
name|primaryKeyOriginal
argument_list|,
name|primaryKeyNew
argument_list|)
return|;
block|}
block|}
end_class

end_unit

