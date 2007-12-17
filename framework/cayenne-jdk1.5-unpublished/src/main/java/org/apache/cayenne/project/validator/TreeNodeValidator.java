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
name|access
operator|.
name|DataNode
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
name|EmbeddedAttribute
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
name|map
operator|.
name|ObjRelationship
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
name|map
operator|.
name|ProcedureParameter
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
name|SQLTemplate
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
name|SelectQuery
import|;
end_import

begin_comment
comment|/**  * Validator of a single node in a project object tree.<i>Do not confuse with  * org.apache.cayenne.access.DataNode.</i>  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|TreeNodeValidator
block|{
comment|// initialize singleton validators
specifier|protected
specifier|static
specifier|final
name|DomainValidator
name|domainValidator
init|=
operator|new
name|DomainValidator
argument_list|()
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|DataNodeValidator
name|nodeValidator
init|=
operator|new
name|DataNodeValidator
argument_list|()
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|DataMapValidator
name|mapValidator
init|=
operator|new
name|DataMapValidator
argument_list|()
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|ObjEntityValidator
name|objEntityValidator
init|=
operator|new
name|ObjEntityValidator
argument_list|()
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|ObjAttributeValidator
name|objAttrValidator
init|=
operator|new
name|ObjAttributeValidator
argument_list|()
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|ObjRelationshipValidator
name|objRelValidator
init|=
operator|new
name|ObjRelationshipValidator
argument_list|()
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|DbEntityValidator
name|dbEntityValidator
init|=
operator|new
name|DbEntityValidator
argument_list|()
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|DbAttributeValidator
name|dbAttrValidator
init|=
operator|new
name|DbAttributeValidator
argument_list|()
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|DbRelationshipValidator
name|dbRelValidator
init|=
operator|new
name|DbRelationshipValidator
argument_list|()
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|ProcedureValidator
name|procedureValidator
init|=
operator|new
name|ProcedureValidator
argument_list|()
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|ProcedureParameterValidator
name|procedureParameterValidator
init|=
operator|new
name|ProcedureParameterValidator
argument_list|()
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|SelectQueryValidator
name|selectQueryValidator
init|=
operator|new
name|SelectQueryValidator
argument_list|()
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|ProcedureQueryValidator
name|procedureQueryValidator
init|=
operator|new
name|ProcedureQueryValidator
argument_list|()
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|SQLTemplateValidator
name|sqlTemplateValidator
init|=
operator|new
name|SQLTemplateValidator
argument_list|()
decl_stmt|;
comment|/**      * Validates an object, appending any validation messages to the validator provided.      */
specifier|public
specifier|static
name|void
name|validate
parameter_list|(
name|ProjectPath
name|path
parameter_list|,
name|Validator
name|validator
parameter_list|)
block|{
name|Object
name|validatedObj
init|=
name|path
operator|.
name|getObject
argument_list|()
decl_stmt|;
name|TreeNodeValidator
name|validatorObj
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|validatedObj
operator|instanceof
name|EmbeddedAttribute
condition|)
block|{
comment|//TODO
return|return;
block|}
if|if
condition|(
name|validatedObj
operator|instanceof
name|ObjAttribute
condition|)
block|{
name|validatorObj
operator|=
name|objAttrValidator
expr_stmt|;
block|}
if|else if
condition|(
name|validatedObj
operator|instanceof
name|ObjRelationship
condition|)
block|{
name|validatorObj
operator|=
name|objRelValidator
expr_stmt|;
block|}
if|else if
condition|(
name|validatedObj
operator|instanceof
name|ObjEntity
condition|)
block|{
name|validatorObj
operator|=
name|objEntityValidator
expr_stmt|;
block|}
if|else if
condition|(
name|validatedObj
operator|instanceof
name|DbAttribute
condition|)
block|{
name|validatorObj
operator|=
name|dbAttrValidator
expr_stmt|;
block|}
if|else if
condition|(
name|validatedObj
operator|instanceof
name|DbRelationship
condition|)
block|{
name|validatorObj
operator|=
name|dbRelValidator
expr_stmt|;
block|}
if|else if
condition|(
name|validatedObj
operator|instanceof
name|DbEntity
condition|)
block|{
name|validatorObj
operator|=
name|dbEntityValidator
expr_stmt|;
block|}
if|else if
condition|(
name|validatedObj
operator|instanceof
name|DataNode
condition|)
block|{
name|validatorObj
operator|=
name|nodeValidator
expr_stmt|;
block|}
if|else if
condition|(
name|validatedObj
operator|instanceof
name|DataMap
condition|)
block|{
name|validatorObj
operator|=
name|mapValidator
expr_stmt|;
block|}
if|else if
condition|(
name|validatedObj
operator|instanceof
name|DataDomain
condition|)
block|{
name|validatorObj
operator|=
name|domainValidator
expr_stmt|;
block|}
if|else if
condition|(
name|validatedObj
operator|instanceof
name|Procedure
condition|)
block|{
name|validatorObj
operator|=
name|procedureValidator
expr_stmt|;
block|}
if|else if
condition|(
name|validatedObj
operator|instanceof
name|ProcedureParameter
condition|)
block|{
name|validatorObj
operator|=
name|procedureParameterValidator
expr_stmt|;
block|}
if|else if
condition|(
name|validatedObj
operator|instanceof
name|SelectQuery
condition|)
block|{
name|validatorObj
operator|=
name|selectQueryValidator
expr_stmt|;
block|}
if|else if
condition|(
name|validatedObj
operator|instanceof
name|SQLTemplate
condition|)
block|{
name|validatorObj
operator|=
name|sqlTemplateValidator
expr_stmt|;
block|}
if|else if
condition|(
name|validatedObj
operator|instanceof
name|ProcedureQuery
condition|)
block|{
name|validatorObj
operator|=
name|procedureQueryValidator
expr_stmt|;
block|}
else|else
block|{
comment|// ignore unknown nodes
return|return;
block|}
name|validatorObj
operator|.
name|validateObject
argument_list|(
name|path
argument_list|,
name|validator
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor for TreeNodeValidator.      */
specifier|public
name|TreeNodeValidator
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
comment|/**      * Validates an object, appending any warnings or errors to the validator. Object to      * be validated is the last object in a<code>treeNodePath</code> array argument.      * Concrete implementations would expect an object of a specific type. Otherwise,      * ClassCastException will be thrown.      */
specifier|public
specifier|abstract
name|void
name|validateObject
parameter_list|(
name|ProjectPath
name|treeNodePath
parameter_list|,
name|Validator
name|validator
parameter_list|)
function_decl|;
block|}
end_class

end_unit

