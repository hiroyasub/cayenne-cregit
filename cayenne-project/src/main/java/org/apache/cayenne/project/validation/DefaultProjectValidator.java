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
name|validation
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
name|configuration
operator|.
name|ConfigurationNode
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
name|configuration
operator|.
name|ConfigurationNodeVisitor
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
name|configuration
operator|.
name|DataChannelDescriptor
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
name|configuration
operator|.
name|DataNodeDescriptor
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
name|EJBQLQueryDescriptor
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
name|Embeddable
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
name|EmbeddableAttribute
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
name|map
operator|.
name|ProcedureQueryDescriptor
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
name|QueryDescriptor
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
name|SQLTemplateDescriptor
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
name|SelectQueryDescriptor
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
name|validation
operator|.
name|ValidationResult
import|;
end_import

begin_comment
comment|/**  * @since 3.1  */
end_comment

begin_class
specifier|public
class|class
name|DefaultProjectValidator
implements|implements
name|ProjectValidator
block|{
specifier|private
name|DataChannelValidator
name|dataChannelValidator
decl_stmt|;
specifier|private
name|DataNodeValidator
name|nodeValidator
decl_stmt|;
specifier|private
name|DataMapValidator
name|mapValidator
decl_stmt|;
specifier|private
name|ObjEntityValidator
name|objEntityValidator
decl_stmt|;
specifier|private
name|ObjAttributeValidator
name|objAttrValidator
decl_stmt|;
specifier|private
name|ObjRelationshipValidator
name|objRelValidator
decl_stmt|;
specifier|private
name|DbEntityValidator
name|dbEntityValidator
decl_stmt|;
specifier|private
name|DbAttributeValidator
name|dbAttrValidator
decl_stmt|;
specifier|private
name|DbRelationshipValidator
name|dbRelValidator
decl_stmt|;
specifier|private
name|EmbeddableAttributeValidator
name|embeddableAttributeValidator
decl_stmt|;
specifier|private
name|EmbeddableValidator
name|embeddableValidator
decl_stmt|;
specifier|private
name|ProcedureValidator
name|procedureValidator
decl_stmt|;
specifier|private
name|ProcedureParameterValidator
name|procedureParameterValidator
decl_stmt|;
specifier|private
name|SelectQueryValidator
name|selectQueryValidator
decl_stmt|;
specifier|private
name|ProcedureQueryValidator
name|procedureQueryValidator
decl_stmt|;
specifier|private
name|EJBQLQueryValidator
name|ejbqlQueryValidator
decl_stmt|;
specifier|private
name|SQLTemplateValidator
name|sqlTemplateValidator
decl_stmt|;
name|DefaultProjectValidator
parameter_list|()
block|{
name|dataChannelValidator
operator|=
operator|new
name|DataChannelValidator
argument_list|()
expr_stmt|;
name|nodeValidator
operator|=
operator|new
name|DataNodeValidator
argument_list|()
expr_stmt|;
name|mapValidator
operator|=
operator|new
name|DataMapValidator
argument_list|()
expr_stmt|;
name|objEntityValidator
operator|=
operator|new
name|ObjEntityValidator
argument_list|()
expr_stmt|;
name|objAttrValidator
operator|=
operator|new
name|ObjAttributeValidator
argument_list|()
expr_stmt|;
name|objRelValidator
operator|=
operator|new
name|ObjRelationshipValidator
argument_list|()
expr_stmt|;
name|dbEntityValidator
operator|=
operator|new
name|DbEntityValidator
argument_list|()
expr_stmt|;
name|dbAttrValidator
operator|=
operator|new
name|DbAttributeValidator
argument_list|()
expr_stmt|;
name|dbRelValidator
operator|=
operator|new
name|DbRelationshipValidator
argument_list|()
expr_stmt|;
name|embeddableAttributeValidator
operator|=
operator|new
name|EmbeddableAttributeValidator
argument_list|()
expr_stmt|;
name|embeddableValidator
operator|=
operator|new
name|EmbeddableValidator
argument_list|()
expr_stmt|;
name|procedureValidator
operator|=
operator|new
name|ProcedureValidator
argument_list|()
expr_stmt|;
name|procedureParameterValidator
operator|=
operator|new
name|ProcedureParameterValidator
argument_list|()
expr_stmt|;
name|selectQueryValidator
operator|=
operator|new
name|SelectQueryValidator
argument_list|()
expr_stmt|;
name|procedureQueryValidator
operator|=
operator|new
name|ProcedureQueryValidator
argument_list|()
expr_stmt|;
name|ejbqlQueryValidator
operator|=
operator|new
name|EJBQLQueryValidator
argument_list|()
expr_stmt|;
name|sqlTemplateValidator
operator|=
operator|new
name|SQLTemplateValidator
argument_list|()
expr_stmt|;
block|}
specifier|public
name|ValidationResult
name|validate
parameter_list|(
name|ConfigurationNode
name|node
parameter_list|)
block|{
return|return
name|node
operator|.
name|acceptVisitor
argument_list|(
operator|new
name|ValidationVisitor
argument_list|()
argument_list|)
return|;
block|}
class|class
name|ValidationVisitor
implements|implements
name|ConfigurationNodeVisitor
argument_list|<
name|ValidationResult
argument_list|>
block|{
specifier|private
name|ValidationResult
name|validationResult
decl_stmt|;
name|ValidationVisitor
parameter_list|()
block|{
name|validationResult
operator|=
operator|new
name|ValidationResult
argument_list|()
expr_stmt|;
block|}
specifier|public
name|ValidationResult
name|visitDataChannelDescriptor
parameter_list|(
name|DataChannelDescriptor
name|channelDescriptor
parameter_list|)
block|{
name|dataChannelValidator
operator|.
name|validate
argument_list|(
name|channelDescriptor
argument_list|,
name|validationResult
argument_list|)
expr_stmt|;
for|for
control|(
name|DataNodeDescriptor
name|node
range|:
name|channelDescriptor
operator|.
name|getNodeDescriptors
argument_list|()
control|)
block|{
name|visitDataNodeDescriptor
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|DataMap
name|map
range|:
name|channelDescriptor
operator|.
name|getDataMaps
argument_list|()
control|)
block|{
name|visitDataMap
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
return|return
name|validationResult
return|;
block|}
specifier|public
name|ValidationResult
name|visitDataMap
parameter_list|(
name|DataMap
name|dataMap
parameter_list|)
block|{
name|mapValidator
operator|.
name|validate
argument_list|(
name|dataMap
argument_list|,
name|validationResult
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|Embeddable
argument_list|>
name|itEmb
init|=
name|dataMap
operator|.
name|getEmbeddables
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|itEmb
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Embeddable
name|emb
init|=
name|itEmb
operator|.
name|next
argument_list|()
decl_stmt|;
name|visitEmbeddable
argument_list|(
name|emb
argument_list|)
expr_stmt|;
block|}
name|Iterator
argument_list|<
name|ObjEntity
argument_list|>
name|itObjEnt
init|=
name|dataMap
operator|.
name|getObjEntities
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|itObjEnt
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ObjEntity
name|ent
init|=
name|itObjEnt
operator|.
name|next
argument_list|()
decl_stmt|;
name|visitObjEntity
argument_list|(
name|ent
argument_list|)
expr_stmt|;
block|}
name|Iterator
argument_list|<
name|DbEntity
argument_list|>
name|itDbEnt
init|=
name|dataMap
operator|.
name|getDbEntities
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|itDbEnt
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|DbEntity
name|ent
init|=
name|itDbEnt
operator|.
name|next
argument_list|()
decl_stmt|;
name|visitDbEntity
argument_list|(
name|ent
argument_list|)
expr_stmt|;
block|}
name|Iterator
argument_list|<
name|Procedure
argument_list|>
name|itProc
init|=
name|dataMap
operator|.
name|getProcedures
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|itProc
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Procedure
name|proc
init|=
name|itProc
operator|.
name|next
argument_list|()
decl_stmt|;
name|visitProcedure
argument_list|(
name|proc
argument_list|)
expr_stmt|;
block|}
name|Iterator
argument_list|<
name|QueryDescriptor
argument_list|>
name|itQuer
init|=
name|dataMap
operator|.
name|getQueryDescriptors
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|itQuer
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|QueryDescriptor
name|q
init|=
name|itQuer
operator|.
name|next
argument_list|()
decl_stmt|;
name|visitQuery
argument_list|(
name|q
argument_list|)
expr_stmt|;
block|}
return|return
name|validationResult
return|;
block|}
specifier|public
name|ValidationResult
name|visitDataNodeDescriptor
parameter_list|(
name|DataNodeDescriptor
name|nodeDescriptor
parameter_list|)
block|{
name|nodeValidator
operator|.
name|validate
argument_list|(
name|nodeDescriptor
argument_list|,
name|validationResult
argument_list|)
expr_stmt|;
return|return
name|validationResult
return|;
block|}
specifier|public
name|ValidationResult
name|visitDbAttribute
parameter_list|(
name|DbAttribute
name|attribute
parameter_list|)
block|{
name|dbAttrValidator
operator|.
name|validate
argument_list|(
name|attribute
argument_list|,
name|validationResult
argument_list|)
expr_stmt|;
return|return
name|validationResult
return|;
block|}
specifier|public
name|ValidationResult
name|visitDbEntity
parameter_list|(
name|DbEntity
name|entity
parameter_list|)
block|{
name|dbEntityValidator
operator|.
name|validate
argument_list|(
name|entity
argument_list|,
name|validationResult
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|DbAttribute
argument_list|>
name|itAttr
init|=
name|entity
operator|.
name|getAttributes
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|itAttr
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|DbAttribute
name|attr
init|=
name|itAttr
operator|.
name|next
argument_list|()
decl_stmt|;
name|visitDbAttribute
argument_list|(
name|attr
argument_list|)
expr_stmt|;
block|}
name|Iterator
argument_list|<
name|DbRelationship
argument_list|>
name|itRel
init|=
name|entity
operator|.
name|getRelationships
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|itRel
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|DbRelationship
name|rel
init|=
name|itRel
operator|.
name|next
argument_list|()
decl_stmt|;
name|visitDbRelationship
argument_list|(
name|rel
argument_list|)
expr_stmt|;
block|}
return|return
name|validationResult
return|;
block|}
specifier|public
name|ValidationResult
name|visitDbRelationship
parameter_list|(
name|DbRelationship
name|relationship
parameter_list|)
block|{
name|dbRelValidator
operator|.
name|validate
argument_list|(
name|relationship
argument_list|,
name|validationResult
argument_list|)
expr_stmt|;
return|return
name|validationResult
return|;
block|}
specifier|public
name|ValidationResult
name|visitEmbeddable
parameter_list|(
name|Embeddable
name|embeddable
parameter_list|)
block|{
name|embeddableValidator
operator|.
name|validate
argument_list|(
name|embeddable
argument_list|,
name|validationResult
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|EmbeddableAttribute
argument_list|>
name|it
init|=
name|embeddable
operator|.
name|getAttributes
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
name|EmbeddableAttribute
name|attr
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|visitEmbeddableAttribute
argument_list|(
name|attr
argument_list|)
expr_stmt|;
block|}
return|return
name|validationResult
return|;
block|}
specifier|public
name|ValidationResult
name|visitEmbeddableAttribute
parameter_list|(
name|EmbeddableAttribute
name|attribute
parameter_list|)
block|{
name|embeddableAttributeValidator
operator|.
name|validate
argument_list|(
name|attribute
argument_list|,
name|validationResult
argument_list|)
expr_stmt|;
return|return
name|validationResult
return|;
block|}
specifier|public
name|ValidationResult
name|visitObjAttribute
parameter_list|(
name|ObjAttribute
name|attribute
parameter_list|)
block|{
name|objAttrValidator
operator|.
name|validate
argument_list|(
name|attribute
argument_list|,
name|validationResult
argument_list|)
expr_stmt|;
return|return
name|validationResult
return|;
block|}
specifier|public
name|ValidationResult
name|visitObjEntity
parameter_list|(
name|ObjEntity
name|entity
parameter_list|)
block|{
name|objEntityValidator
operator|.
name|validate
argument_list|(
name|entity
argument_list|,
name|validationResult
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|ObjAttribute
argument_list|>
name|itAttr
init|=
name|entity
operator|.
name|getAttributes
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|itAttr
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ObjAttribute
name|attr
init|=
name|itAttr
operator|.
name|next
argument_list|()
decl_stmt|;
name|visitObjAttribute
argument_list|(
name|attr
argument_list|)
expr_stmt|;
block|}
name|Iterator
argument_list|<
name|ObjRelationship
argument_list|>
name|itRel
init|=
name|entity
operator|.
name|getRelationships
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|itRel
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ObjRelationship
name|rel
init|=
name|itRel
operator|.
name|next
argument_list|()
decl_stmt|;
name|visitObjRelationship
argument_list|(
name|rel
argument_list|)
expr_stmt|;
block|}
return|return
name|validationResult
return|;
block|}
specifier|public
name|ValidationResult
name|visitObjRelationship
parameter_list|(
name|ObjRelationship
name|relationship
parameter_list|)
block|{
name|objRelValidator
operator|.
name|validate
argument_list|(
name|relationship
argument_list|,
name|validationResult
argument_list|)
expr_stmt|;
return|return
name|validationResult
return|;
block|}
specifier|public
name|ValidationResult
name|visitProcedure
parameter_list|(
name|Procedure
name|procedure
parameter_list|)
block|{
name|procedureValidator
operator|.
name|validate
argument_list|(
name|procedure
argument_list|,
name|validationResult
argument_list|)
expr_stmt|;
name|ProcedureParameter
name|parameter
init|=
name|procedure
operator|.
name|getResultParam
argument_list|()
decl_stmt|;
if|if
condition|(
name|parameter
operator|!=
literal|null
condition|)
block|{
name|visitProcedureParameter
argument_list|(
name|parameter
argument_list|)
expr_stmt|;
block|}
name|Iterator
argument_list|<
name|ProcedureParameter
argument_list|>
name|itPrOut
init|=
name|procedure
operator|.
name|getCallOutParameters
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|itPrOut
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ProcedureParameter
name|procPar
init|=
name|itPrOut
operator|.
name|next
argument_list|()
decl_stmt|;
name|visitProcedureParameter
argument_list|(
name|procPar
argument_list|)
expr_stmt|;
block|}
name|Iterator
argument_list|<
name|ProcedureParameter
argument_list|>
name|itPr
init|=
name|procedure
operator|.
name|getCallParameters
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|itPr
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ProcedureParameter
name|procPar
init|=
name|itPr
operator|.
name|next
argument_list|()
decl_stmt|;
name|visitProcedureParameter
argument_list|(
name|procPar
argument_list|)
expr_stmt|;
block|}
return|return
name|validationResult
return|;
block|}
specifier|public
name|ValidationResult
name|visitProcedureParameter
parameter_list|(
name|ProcedureParameter
name|parameter
parameter_list|)
block|{
name|procedureParameterValidator
operator|.
name|validate
argument_list|(
name|parameter
argument_list|,
name|validationResult
argument_list|)
expr_stmt|;
return|return
name|validationResult
return|;
block|}
specifier|public
name|ValidationResult
name|visitQuery
parameter_list|(
name|QueryDescriptor
name|query
parameter_list|)
block|{
switch|switch
condition|(
name|query
operator|.
name|getType
argument_list|()
condition|)
block|{
case|case
name|QueryDescriptor
operator|.
name|SELECT_QUERY
case|:
name|selectQueryValidator
operator|.
name|validate
argument_list|(
operator|(
name|SelectQueryDescriptor
operator|)
name|query
argument_list|,
name|validationResult
argument_list|)
expr_stmt|;
break|break;
case|case
name|QueryDescriptor
operator|.
name|SQL_TEMPLATE
case|:
name|sqlTemplateValidator
operator|.
name|validate
argument_list|(
operator|(
name|SQLTemplateDescriptor
operator|)
name|query
argument_list|,
name|validationResult
argument_list|)
expr_stmt|;
break|break;
case|case
name|QueryDescriptor
operator|.
name|PROCEDURE_QUERY
case|:
name|procedureQueryValidator
operator|.
name|validate
argument_list|(
operator|(
name|ProcedureQueryDescriptor
operator|)
name|query
argument_list|,
name|validationResult
argument_list|)
expr_stmt|;
break|break;
case|case
name|QueryDescriptor
operator|.
name|EJBQL_QUERY
case|:
name|ejbqlQueryValidator
operator|.
name|validate
argument_list|(
operator|(
name|EJBQLQueryDescriptor
operator|)
name|query
argument_list|,
name|validationResult
argument_list|)
expr_stmt|;
break|break;
block|}
return|return
name|validationResult
return|;
block|}
block|}
block|}
end_class

end_unit

