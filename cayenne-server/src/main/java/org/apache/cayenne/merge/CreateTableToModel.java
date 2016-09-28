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
name|map
operator|.
name|naming
operator|.
name|NameConverter
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
name|EntityMergeSupport
import|;
end_import

begin_comment
comment|/**  * A {@link MergerToken} to add a {@link DbEntity} to a {@link DataMap}  *   */
end_comment

begin_class
specifier|public
class|class
name|CreateTableToModel
extends|extends
name|AbstractToModelToken
operator|.
name|Entity
block|{
comment|/**      * className if {@link ObjEntity} should be generated with a      *  special class name.      * Setting this to<code>null</code>, because by default class name should be generated       */
specifier|private
name|String
name|objEntityClassName
init|=
literal|null
decl_stmt|;
comment|//CayenneDataObject.class.getName();
specifier|public
name|CreateTableToModel
parameter_list|(
name|DbEntity
name|entity
parameter_list|)
block|{
name|super
argument_list|(
literal|"Create Table"
argument_list|,
name|entity
argument_list|)
expr_stmt|;
block|}
comment|/**      * Set the {@link ObjEntity} className if {@link ObjEntity} should be generated with a      * special class name. Set to null if the {@link ObjEntity} should be created with a      * name based on {@link DataMap#getDefaultPackage()} and {@link ObjEntity#getName()}      *<p>      * The default value is<code>null</code>      */
specifier|public
name|void
name|setObjEntityClassName
parameter_list|(
name|String
name|n
parameter_list|)
block|{
name|objEntityClassName
operator|=
name|n
expr_stmt|;
block|}
specifier|public
name|void
name|execute
parameter_list|(
name|MergerContext
name|mergerContext
parameter_list|)
block|{
name|DataMap
name|map
init|=
name|mergerContext
operator|.
name|getDataMap
argument_list|()
decl_stmt|;
name|map
operator|.
name|addDbEntity
argument_list|(
name|getEntity
argument_list|()
argument_list|)
expr_stmt|;
comment|// create a ObjEntity
name|String
name|objEntityName
init|=
name|NameConverter
operator|.
name|underscoredToJava
argument_list|(
name|getEntity
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
literal|true
argument_list|)
decl_stmt|;
comment|// this loop will terminate even if no valid name is found
comment|// to prevent loader from looping forever (though such case is very unlikely)
name|String
name|baseName
init|=
name|objEntityName
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
literal|1000
operator|&&
name|map
operator|.
name|getObjEntity
argument_list|(
name|objEntityName
argument_list|)
operator|!=
literal|null
condition|;
name|i
operator|++
control|)
block|{
name|objEntityName
operator|=
name|baseName
operator|+
name|i
expr_stmt|;
block|}
name|ObjEntity
name|objEntity
init|=
operator|new
name|ObjEntity
argument_list|(
name|objEntityName
argument_list|)
decl_stmt|;
name|objEntity
operator|.
name|setDbEntity
argument_list|(
name|getEntity
argument_list|()
argument_list|)
expr_stmt|;
comment|// try to find a class name for the ObjEntity
name|String
name|className
init|=
name|objEntityClassName
decl_stmt|;
if|if
condition|(
name|className
operator|==
literal|null
condition|)
block|{
comment|// we should generate a className based on the objEntityName
name|className
operator|=
name|map
operator|.
name|getNameWithDefaultPackage
argument_list|(
name|objEntityName
argument_list|)
expr_stmt|;
block|}
name|objEntity
operator|.
name|setClassName
argument_list|(
name|className
argument_list|)
expr_stmt|;
name|objEntity
operator|.
name|setSuperClassName
argument_list|(
name|map
operator|.
name|getDefaultSuperclass
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|map
operator|.
name|isClientSupported
argument_list|()
condition|)
block|{
name|objEntity
operator|.
name|setClientClassName
argument_list|(
name|map
operator|.
name|getNameWithDefaultClientPackage
argument_list|(
name|objEntity
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|objEntity
operator|.
name|setClientSuperClassName
argument_list|(
name|map
operator|.
name|getDefaultClientSuperclass
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|map
operator|.
name|addObjEntity
argument_list|(
name|objEntity
argument_list|)
expr_stmt|;
comment|// presumably there are no other ObjEntities pointing to this DbEntity, so syncing just this one...
comment|// TODO: use EntityMergeSupport from DbImportConfiguration... otherwise we are ignoring a bunch of
comment|// important settings
operator|new
name|EntityMergeSupport
argument_list|(
name|map
argument_list|)
operator|.
name|synchronizeWithDbEntity
argument_list|(
name|objEntity
argument_list|)
expr_stmt|;
name|mergerContext
operator|.
name|getModelMergeDelegate
argument_list|()
operator|.
name|dbEntityAdded
argument_list|(
name|getEntity
argument_list|()
argument_list|)
expr_stmt|;
name|mergerContext
operator|.
name|getModelMergeDelegate
argument_list|()
operator|.
name|objEntityAdded
argument_list|(
name|objEntity
argument_list|)
expr_stmt|;
block|}
specifier|public
name|MergerToken
name|createReverse
parameter_list|(
name|MergerFactory
name|factory
parameter_list|)
block|{
return|return
name|factory
operator|.
name|createDropTableToDb
argument_list|(
name|getEntity
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

