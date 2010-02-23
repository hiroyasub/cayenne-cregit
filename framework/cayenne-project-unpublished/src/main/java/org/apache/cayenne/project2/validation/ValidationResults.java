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
name|project2
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
name|query
operator|.
name|EJBQLQuery
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

begin_class
specifier|public
class|class
name|ValidationResults
block|{
specifier|private
name|List
argument_list|<
name|ValidationInfo
argument_list|>
name|validationResults
decl_stmt|;
specifier|private
name|int
name|maxSeverity
decl_stmt|;
specifier|public
name|ValidationResults
parameter_list|(
name|ConfigurationNode
name|node
parameter_list|,
name|DefaultProjectValidator
name|defaultProjectValidator
parameter_list|)
block|{
name|ValidationVisitor
name|vis
init|=
operator|new
name|ValidationVisitor
argument_list|(
name|defaultProjectValidator
argument_list|)
decl_stmt|;
name|validationResults
operator|=
name|node
operator|.
name|acceptVisitor
argument_list|(
name|vis
argument_list|)
expr_stmt|;
name|this
operator|.
name|maxSeverity
operator|=
name|vis
operator|.
name|getMaxSeverity
argument_list|()
expr_stmt|;
block|}
specifier|public
name|int
name|getMaxSeverity
parameter_list|()
block|{
return|return
name|maxSeverity
return|;
block|}
specifier|public
name|List
argument_list|<
name|ValidationInfo
argument_list|>
name|getValidationResults
parameter_list|()
block|{
return|return
name|validationResults
return|;
block|}
block|}
end_class

begin_class
class|class
name|ValidationVisitor
implements|implements
name|ConfigurationNodeVisitor
argument_list|<
name|List
argument_list|<
name|ValidationInfo
argument_list|>
argument_list|>
block|{
specifier|private
name|List
argument_list|<
name|ValidationInfo
argument_list|>
name|validationResults
init|=
operator|new
name|ArrayList
argument_list|<
name|ValidationInfo
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|int
name|maxSeverity
decl_stmt|;
specifier|private
name|DefaultProjectValidator
name|defaultProjectValidator
decl_stmt|;
name|int
name|getMaxSeverity
parameter_list|()
block|{
return|return
name|maxSeverity
return|;
block|}
name|ValidationVisitor
parameter_list|(
name|DefaultProjectValidator
name|defaultProjectValidator
parameter_list|)
block|{
name|this
operator|.
name|defaultProjectValidator
operator|=
name|defaultProjectValidator
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|ValidationInfo
argument_list|>
name|visitDataChannelDescriptor
parameter_list|(
name|DataChannelDescriptor
name|channelDescriptor
parameter_list|)
block|{
name|defaultProjectValidator
operator|.
name|getDataChannelValidator
argument_list|()
operator|.
name|validate
argument_list|(
name|channelDescriptor
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|DataNodeDescriptor
argument_list|>
name|it
init|=
name|channelDescriptor
operator|.
name|getNodeDescriptors
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
name|DataNodeDescriptor
name|node
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|visitDataNodeDescriptor
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
name|Iterator
argument_list|<
name|DataMap
argument_list|>
name|itMap
init|=
name|channelDescriptor
operator|.
name|getDataMaps
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|itMap
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|DataMap
name|map
init|=
name|itMap
operator|.
name|next
argument_list|()
decl_stmt|;
name|visitDataMap
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
return|return
name|validationResults
return|;
block|}
specifier|public
name|List
argument_list|<
name|ValidationInfo
argument_list|>
name|visitDataMap
parameter_list|(
name|DataMap
name|dataMap
parameter_list|)
block|{
name|defaultProjectValidator
operator|.
name|getMapValidator
argument_list|()
operator|.
name|validate
argument_list|(
name|dataMap
argument_list|,
name|this
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
name|Query
argument_list|>
name|itQuer
init|=
name|dataMap
operator|.
name|getQueries
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
name|Query
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
name|validationResults
return|;
block|}
specifier|public
name|List
argument_list|<
name|ValidationInfo
argument_list|>
name|visitDataNodeDescriptor
parameter_list|(
name|DataNodeDescriptor
name|nodeDescriptor
parameter_list|)
block|{
name|defaultProjectValidator
operator|.
name|getNodeValidator
argument_list|()
operator|.
name|validate
argument_list|(
name|nodeDescriptor
argument_list|,
name|this
argument_list|)
expr_stmt|;
return|return
name|validationResults
return|;
block|}
specifier|public
name|List
argument_list|<
name|ValidationInfo
argument_list|>
name|visitDbAttribute
parameter_list|(
name|DbAttribute
name|attribute
parameter_list|)
block|{
name|defaultProjectValidator
operator|.
name|getDbAttrValidator
argument_list|()
operator|.
name|validate
argument_list|(
name|attribute
argument_list|,
name|this
argument_list|)
expr_stmt|;
return|return
name|validationResults
return|;
block|}
specifier|public
name|List
argument_list|<
name|ValidationInfo
argument_list|>
name|visitDbEntity
parameter_list|(
name|DbEntity
name|entity
parameter_list|)
block|{
name|defaultProjectValidator
operator|.
name|getDbEntityValidator
argument_list|()
operator|.
name|validate
argument_list|(
name|entity
argument_list|,
name|this
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
name|validationResults
return|;
block|}
specifier|public
name|List
argument_list|<
name|ValidationInfo
argument_list|>
name|visitDbRelationship
parameter_list|(
name|DbRelationship
name|relationship
parameter_list|)
block|{
name|defaultProjectValidator
operator|.
name|getDbRelValidator
argument_list|()
operator|.
name|validate
argument_list|(
name|relationship
argument_list|,
name|this
argument_list|)
expr_stmt|;
return|return
name|validationResults
return|;
block|}
specifier|public
name|List
argument_list|<
name|ValidationInfo
argument_list|>
name|visitEmbeddable
parameter_list|(
name|Embeddable
name|embeddable
parameter_list|)
block|{
name|defaultProjectValidator
operator|.
name|getEmbeddableValidator
argument_list|()
operator|.
name|validate
argument_list|(
name|embeddable
argument_list|,
name|this
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
name|validationResults
return|;
block|}
specifier|public
name|List
argument_list|<
name|ValidationInfo
argument_list|>
name|visitEmbeddableAttribute
parameter_list|(
name|EmbeddableAttribute
name|attribute
parameter_list|)
block|{
name|defaultProjectValidator
operator|.
name|getEmbeddableAttributeValidator
argument_list|()
operator|.
name|validate
argument_list|(
name|attribute
argument_list|,
name|this
argument_list|)
expr_stmt|;
return|return
name|validationResults
return|;
block|}
specifier|public
name|List
argument_list|<
name|ValidationInfo
argument_list|>
name|visitObjAttribute
parameter_list|(
name|ObjAttribute
name|attribute
parameter_list|)
block|{
name|defaultProjectValidator
operator|.
name|getObjAttrValidator
argument_list|()
operator|.
name|validate
argument_list|(
name|attribute
argument_list|,
name|this
argument_list|)
expr_stmt|;
return|return
name|validationResults
return|;
block|}
specifier|public
name|List
argument_list|<
name|ValidationInfo
argument_list|>
name|visitObjEntity
parameter_list|(
name|ObjEntity
name|entity
parameter_list|)
block|{
name|defaultProjectValidator
operator|.
name|getObjEntityValidator
argument_list|()
operator|.
name|validate
argument_list|(
name|entity
argument_list|,
name|this
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
name|validationResults
return|;
block|}
specifier|public
name|List
argument_list|<
name|ValidationInfo
argument_list|>
name|visitObjRelationship
parameter_list|(
name|ObjRelationship
name|relationship
parameter_list|)
block|{
name|defaultProjectValidator
operator|.
name|getObjRelValidator
argument_list|()
operator|.
name|validate
argument_list|(
name|relationship
argument_list|,
name|this
argument_list|)
expr_stmt|;
return|return
name|validationResults
return|;
block|}
specifier|public
name|List
argument_list|<
name|ValidationInfo
argument_list|>
name|visitProcedure
parameter_list|(
name|Procedure
name|procedure
parameter_list|)
block|{
name|defaultProjectValidator
operator|.
name|getProcedureValidator
argument_list|()
operator|.
name|validate
argument_list|(
name|procedure
argument_list|,
name|this
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
name|visitProcedureParameter
argument_list|(
name|parameter
argument_list|)
expr_stmt|;
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
name|validationResults
return|;
block|}
specifier|public
name|List
argument_list|<
name|ValidationInfo
argument_list|>
name|visitProcedureParameter
parameter_list|(
name|ProcedureParameter
name|parameter
parameter_list|)
block|{
name|defaultProjectValidator
operator|.
name|getProcedureParameterValidator
argument_list|()
operator|.
name|validate
argument_list|(
name|parameter
argument_list|,
name|this
argument_list|)
expr_stmt|;
return|return
name|validationResults
return|;
block|}
specifier|public
name|List
argument_list|<
name|ValidationInfo
argument_list|>
name|visitQuery
parameter_list|(
name|Query
name|query
parameter_list|)
block|{
if|if
condition|(
name|query
operator|instanceof
name|SelectQuery
condition|)
block|{
name|defaultProjectValidator
operator|.
name|getSelectQueryValidator
argument_list|()
operator|.
name|validate
argument_list|(
name|query
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|query
operator|instanceof
name|SQLTemplate
condition|)
block|{
name|defaultProjectValidator
operator|.
name|getSqlTemplateValidator
argument_list|()
operator|.
name|validate
argument_list|(
name|query
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|query
operator|instanceof
name|ProcedureQuery
condition|)
block|{
name|defaultProjectValidator
operator|.
name|getProcedureQueryValidator
argument_list|()
operator|.
name|validate
argument_list|(
name|query
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|query
operator|instanceof
name|EJBQLQuery
condition|)
block|{
name|defaultProjectValidator
operator|.
name|getEjbqlQueryValidator
argument_list|()
operator|.
name|validate
argument_list|(
name|query
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// ignore unknown nodes
return|return
literal|null
return|;
block|}
return|return
name|validationResults
return|;
block|}
comment|/**      * Registers validation result. Increases internally stored max severity if      *<code>result</code> parameter has a higher severity then the current value. Leaves      * current value unchanged otherwise.      */
specifier|public
name|void
name|registerValidated
parameter_list|(
name|int
name|severity
parameter_list|,
name|String
name|message
parameter_list|,
name|Object
name|object
parameter_list|)
block|{
name|ValidationInfo
name|result
init|=
operator|new
name|ValidationInfo
argument_list|(
name|severity
argument_list|,
name|message
argument_list|,
name|object
argument_list|)
decl_stmt|;
name|validationResults
operator|.
name|add
argument_list|(
name|result
argument_list|)
expr_stmt|;
if|if
condition|(
name|maxSeverity
operator|<
name|severity
condition|)
block|{
name|maxSeverity
operator|=
name|severity
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|registerError
parameter_list|(
name|String
name|message
parameter_list|,
name|Object
name|object
parameter_list|)
block|{
name|registerValidated
argument_list|(
name|ValidationInfo
operator|.
name|ERROR
argument_list|,
name|message
argument_list|,
name|object
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|registerWarning
parameter_list|(
name|String
name|message
parameter_list|,
name|Object
name|object
parameter_list|)
block|{
name|registerValidated
argument_list|(
name|ValidationInfo
operator|.
name|WARNING
argument_list|,
name|message
argument_list|,
name|object
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

