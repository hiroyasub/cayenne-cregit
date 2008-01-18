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

begin_comment
comment|/**  * A {@link MergerToken} to remove a {@link DbAttribute} from a {@link DbEntity}.  *   * @author halset  */
end_comment

begin_class
specifier|public
class|class
name|DropColumnToModel
extends|extends
name|AbstractToModelToken
block|{
specifier|private
name|DbEntity
name|entity
decl_stmt|;
specifier|private
name|DbAttribute
name|column
decl_stmt|;
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
name|this
operator|.
name|entity
operator|=
name|entity
expr_stmt|;
name|this
operator|.
name|column
operator|=
name|column
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
name|createAddColumnToDb
argument_list|(
name|entity
argument_list|,
name|column
argument_list|)
return|;
block|}
specifier|public
name|void
name|execute
parameter_list|(
name|MergerContext
name|mergerContext
parameter_list|)
block|{
comment|// remove ObjAttribute mapped to same column
for|for
control|(
name|ObjEntity
name|objEntity
range|:
name|entity
operator|.
name|getDataMap
argument_list|()
operator|.
name|getObjEntities
argument_list|()
control|)
block|{
if|if
condition|(
name|objEntity
operator|.
name|getDbEntity
argument_list|()
operator|.
name|equals
argument_list|(
name|entity
argument_list|)
condition|)
block|{
name|ObjAttribute
name|objAttribute
init|=
name|objEntity
operator|.
name|getAttributeForDbAttribute
argument_list|(
name|column
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
block|}
block|}
block|}
comment|// remove DbAttribute
name|entity
operator|.
name|removeAttribute
argument_list|(
name|column
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getTokenName
parameter_list|()
block|{
return|return
literal|"Drop Column"
return|;
block|}
specifier|public
name|String
name|getTokenValue
parameter_list|()
block|{
return|return
name|entity
operator|.
name|getName
argument_list|()
operator|+
literal|"."
operator|+
name|column
operator|.
name|getName
argument_list|()
return|;
block|}
block|}
end_class

end_unit

