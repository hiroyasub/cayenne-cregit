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
name|project
operator|.
name|validator
package|;
end_package

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
name|project
operator|.
name|ProjectPath
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
name|Util
import|;
end_import

begin_comment
comment|/**  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|DbEntityValidator
extends|extends
name|TreeNodeValidator
block|{
comment|/** 	 * Constructor for DbEntityValidator. 	 */
specifier|public
name|DbEntityValidator
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|validateObject
parameter_list|(
name|ProjectPath
name|path
parameter_list|,
name|Validator
name|validator
parameter_list|)
block|{
name|DbEntity
name|ent
init|=
operator|(
name|DbEntity
operator|)
name|path
operator|.
name|getObject
argument_list|()
decl_stmt|;
name|validateName
argument_list|(
name|ent
argument_list|,
name|path
argument_list|,
name|validator
argument_list|)
expr_stmt|;
name|validateAttributes
argument_list|(
name|ent
argument_list|,
name|path
argument_list|,
name|validator
argument_list|)
expr_stmt|;
name|validatePK
argument_list|(
name|ent
argument_list|,
name|path
argument_list|,
name|validator
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Validates the presence of the primary key. A warning is given only if the parent 	 * map also conatins an ObjEntity mapped to this entity, since unmapped primary key 	 * is ok if working with data rows. 	 */
specifier|protected
name|void
name|validatePK
parameter_list|(
name|DbEntity
name|ent
parameter_list|,
name|ProjectPath
name|path
parameter_list|,
name|Validator
name|validator
parameter_list|)
block|{
if|if
condition|(
name|ent
operator|.
name|getAttributes
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|0
operator|&&
name|ent
operator|.
name|getPrimaryKeys
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
name|DataMap
name|map
init|=
name|ent
operator|.
name|getDataMap
argument_list|()
decl_stmt|;
if|if
condition|(
name|map
operator|!=
literal|null
operator|&&
name|map
operator|.
name|getMappedEntities
argument_list|(
name|ent
argument_list|)
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
comment|// there is an objentity, so complain about no pk
name|validator
operator|.
name|registerWarning
argument_list|(
literal|"DbEntity \""
operator|+
name|ent
operator|.
name|getName
argument_list|()
operator|+
literal|"\" has no primary key attributes defined."
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/** 	 * Tables must have columns. 	 */
specifier|protected
name|void
name|validateAttributes
parameter_list|(
name|DbEntity
name|ent
parameter_list|,
name|ProjectPath
name|path
parameter_list|,
name|Validator
name|validator
parameter_list|)
block|{
if|if
condition|(
name|ent
operator|.
name|getAttributes
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
comment|// complain about missing attributes
name|validator
operator|.
name|registerWarning
argument_list|(
literal|"DbEntity \""
operator|+
name|ent
operator|.
name|getName
argument_list|()
operator|+
literal|"\" has no attributes defined."
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|validateName
parameter_list|(
name|DbEntity
name|ent
parameter_list|,
name|ProjectPath
name|path
parameter_list|,
name|Validator
name|validator
parameter_list|)
block|{
name|String
name|name
init|=
name|ent
operator|.
name|getName
argument_list|()
decl_stmt|;
comment|// Must have name
if|if
condition|(
name|Util
operator|.
name|isEmptyString
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|validator
operator|.
name|registerError
argument_list|(
literal|"Unnamed DbEntity."
argument_list|,
name|path
argument_list|)
expr_stmt|;
return|return;
block|}
name|DataMap
name|map
init|=
operator|(
name|DataMap
operator|)
name|path
operator|.
name|getObjectParent
argument_list|()
decl_stmt|;
if|if
condition|(
name|map
operator|==
literal|null
condition|)
block|{
return|return;
block|}
comment|// check for duplicate names in the parent context
name|Iterator
name|it
init|=
name|map
operator|.
name|getDbEntities
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|DbEntity
name|otherEnt
init|=
operator|(
name|DbEntity
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|otherEnt
operator|==
name|ent
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|name
operator|.
name|equals
argument_list|(
name|otherEnt
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|validator
operator|.
name|registerError
argument_list|(
literal|"Duplicate DbEntity name: "
operator|+
name|name
operator|+
literal|"."
argument_list|,
name|path
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
block|}
end_class

end_unit

