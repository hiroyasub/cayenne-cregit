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
name|Procedure
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
name|query
operator|.
name|ProcedureQuery
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
name|query
operator|.
name|Query
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
comment|/**  * Validator for ProcedureQueries.  *   * @author Andrus Adamchik  * @since 1.1  */
end_comment

begin_class
specifier|public
class|class
name|ProcedureQueryValidator
extends|extends
name|TreeNodeValidator
block|{
annotation|@
name|Override
specifier|public
name|void
name|validateObject
parameter_list|(
name|ProjectPath
name|treeNodePath
parameter_list|,
name|Validator
name|validator
parameter_list|)
block|{
name|ProcedureQuery
name|query
init|=
operator|(
name|ProcedureQuery
operator|)
name|treeNodePath
operator|.
name|getObject
argument_list|()
decl_stmt|;
name|validateName
argument_list|(
name|query
argument_list|,
name|treeNodePath
argument_list|,
name|validator
argument_list|)
expr_stmt|;
name|validateRoot
argument_list|(
name|query
argument_list|,
name|treeNodePath
argument_list|,
name|validator
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|validateRoot
parameter_list|(
name|ProcedureQuery
name|query
parameter_list|,
name|ProjectPath
name|path
parameter_list|,
name|Validator
name|validator
parameter_list|)
block|{
name|DataMap
name|map
init|=
name|path
operator|.
name|firstInstanceOf
argument_list|(
name|DataMap
operator|.
name|class
argument_list|)
decl_stmt|;
name|Object
name|root
init|=
name|query
operator|.
name|getRoot
argument_list|()
decl_stmt|;
if|if
condition|(
name|root
operator|==
literal|null
operator|&&
name|map
operator|!=
literal|null
condition|)
block|{
name|validator
operator|.
name|registerWarning
argument_list|(
literal|"Query has no root"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
comment|// procedure query only supports procedure root
if|if
condition|(
name|root
operator|instanceof
name|Procedure
condition|)
block|{
name|Procedure
name|procedure
init|=
operator|(
name|Procedure
operator|)
name|root
decl_stmt|;
comment|// procedure may have been deleted...
if|if
condition|(
name|map
operator|!=
literal|null
operator|&&
name|map
operator|.
name|getProcedure
argument_list|(
name|procedure
operator|.
name|getName
argument_list|()
argument_list|)
operator|!=
name|procedure
condition|)
block|{
name|validator
operator|.
name|registerWarning
argument_list|(
literal|"Invalid Procedure Root - "
operator|+
name|procedure
operator|.
name|getName
argument_list|()
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
return|return;
block|}
if|if
condition|(
name|root
operator|instanceof
name|String
condition|)
block|{
if|if
condition|(
name|map
operator|!=
literal|null
operator|&&
name|map
operator|.
name|getProcedure
argument_list|(
name|root
operator|.
name|toString
argument_list|()
argument_list|)
operator|==
literal|null
condition|)
block|{
name|validator
operator|.
name|registerWarning
argument_list|(
literal|"Invalid Procedure Root - "
operator|+
name|root
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|void
name|validateName
parameter_list|(
name|Query
name|query
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
name|query
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
literal|"Unnamed Query."
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
for|for
control|(
specifier|final
name|Query
name|otherQuery
range|:
name|map
operator|.
name|getQueries
argument_list|()
control|)
block|{
if|if
condition|(
name|otherQuery
operator|==
name|query
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
name|otherQuery
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
literal|"Duplicate Query name: "
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

