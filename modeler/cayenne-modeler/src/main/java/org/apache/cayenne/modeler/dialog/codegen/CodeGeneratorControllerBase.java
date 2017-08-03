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
name|modeler
operator|.
name|dialog
operator|.
name|codegen
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
name|modeler
operator|.
name|util
operator|.
name|CayenneController
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
name|modeler
operator|.
name|util
operator|.
name|CellRenderers
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
name|ValidationFailure
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

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|Icon
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JLabel
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Component
import|;
end_import

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
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Predicate
import|;
end_import

begin_comment
comment|/**  * A base superclass of a top controller for the code generator. Defines all common model  * parts used in class generation.  *  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|CodeGeneratorControllerBase
extends|extends
name|CayenneController
block|{
specifier|public
specifier|static
specifier|final
name|String
name|SELECTED_PROPERTY
init|=
literal|"selected"
decl_stmt|;
specifier|protected
name|Collection
argument_list|<
name|DataMap
argument_list|>
name|dataMaps
decl_stmt|;
specifier|protected
name|ValidationResult
name|validation
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|Object
argument_list|>
name|classes
decl_stmt|;
specifier|protected
name|Set
argument_list|<
name|String
argument_list|>
name|selectedEntities
decl_stmt|;
specifier|protected
name|Set
argument_list|<
name|String
argument_list|>
name|selectedEmbeddables
decl_stmt|;
specifier|protected
specifier|transient
name|Object
name|currentClass
decl_stmt|;
specifier|public
name|CodeGeneratorControllerBase
parameter_list|(
name|CayenneController
name|parent
parameter_list|,
name|Collection
argument_list|<
name|DataMap
argument_list|>
name|dataMaps
parameter_list|)
block|{
name|super
argument_list|(
name|parent
argument_list|)
expr_stmt|;
name|this
operator|.
name|dataMaps
operator|=
name|dataMaps
expr_stmt|;
name|this
operator|.
name|classes
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
for|for
control|(
name|DataMap
name|dataMap
range|:
name|dataMaps
control|)
block|{
name|this
operator|.
name|classes
operator|.
name|addAll
argument_list|(
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|dataMap
operator|.
name|getObjEntities
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|classes
operator|.
name|addAll
argument_list|(
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|dataMap
operator|.
name|getEmbeddables
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|selectedEntities
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|selectedEmbeddables
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|Object
argument_list|>
name|getClasses
parameter_list|()
block|{
return|return
name|classes
return|;
block|}
specifier|public
specifier|abstract
name|Component
name|getView
parameter_list|()
function_decl|;
specifier|public
name|void
name|validate
parameter_list|(
name|GeneratorController
name|validator
parameter_list|)
block|{
name|ValidationResult
name|validationBuffer
init|=
operator|new
name|ValidationResult
argument_list|()
decl_stmt|;
if|if
condition|(
name|validator
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Object
name|classObj
range|:
name|classes
control|)
block|{
if|if
condition|(
name|classObj
operator|instanceof
name|ObjEntity
condition|)
block|{
name|validator
operator|.
name|validateEntity
argument_list|(
name|validationBuffer
argument_list|,
operator|(
name|ObjEntity
operator|)
name|classObj
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|classObj
operator|instanceof
name|Embeddable
condition|)
block|{
name|validator
operator|.
name|validateEmbeddable
argument_list|(
name|validationBuffer
argument_list|,
operator|(
name|Embeddable
operator|)
name|classObj
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|this
operator|.
name|validation
operator|=
name|validationBuffer
expr_stmt|;
block|}
specifier|public
name|boolean
name|updateSelection
parameter_list|(
name|Predicate
argument_list|<
name|Object
argument_list|>
name|predicate
parameter_list|)
block|{
name|boolean
name|modified
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Object
name|classObj
range|:
name|classes
control|)
block|{
name|boolean
name|select
init|=
name|predicate
operator|.
name|test
argument_list|(
name|classObj
argument_list|)
decl_stmt|;
if|if
condition|(
name|classObj
operator|instanceof
name|ObjEntity
condition|)
block|{
if|if
condition|(
name|select
condition|)
block|{
if|if
condition|(
name|selectedEntities
operator|.
name|add
argument_list|(
operator|(
operator|(
name|ObjEntity
operator|)
name|classObj
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|modified
operator|=
literal|true
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|selectedEntities
operator|.
name|remove
argument_list|(
operator|(
operator|(
name|ObjEntity
operator|)
name|classObj
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|modified
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
if|else if
condition|(
name|classObj
operator|instanceof
name|Embeddable
condition|)
block|{
if|if
condition|(
name|select
condition|)
block|{
if|if
condition|(
name|selectedEmbeddables
operator|.
name|add
argument_list|(
operator|(
operator|(
name|Embeddable
operator|)
name|classObj
operator|)
operator|.
name|getClassName
argument_list|()
argument_list|)
condition|)
block|{
name|modified
operator|=
literal|true
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|selectedEmbeddables
operator|.
name|remove
argument_list|(
operator|(
operator|(
name|Embeddable
operator|)
name|classObj
operator|)
operator|.
name|getClassName
argument_list|()
argument_list|)
condition|)
block|{
name|modified
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
block|}
if|if
condition|(
name|modified
condition|)
block|{
name|firePropertyChange
argument_list|(
name|SELECTED_PROPERTY
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
return|return
name|modified
return|;
block|}
specifier|public
name|List
argument_list|<
name|Embeddable
argument_list|>
name|getSelectedEmbeddables
parameter_list|()
block|{
name|List
argument_list|<
name|Embeddable
argument_list|>
name|selected
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|selectedEmbeddables
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Object
name|classObj
range|:
name|classes
control|)
block|{
if|if
condition|(
name|classObj
operator|instanceof
name|Embeddable
operator|&&
name|selectedEmbeddables
operator|.
name|contains
argument_list|(
operator|(
operator|(
name|Embeddable
operator|)
name|classObj
operator|)
operator|.
name|getClassName
argument_list|()
argument_list|)
condition|)
block|{
name|selected
operator|.
name|add
argument_list|(
operator|(
name|Embeddable
operator|)
name|classObj
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|selected
return|;
block|}
specifier|public
name|List
argument_list|<
name|ObjEntity
argument_list|>
name|getSelectedEntities
parameter_list|()
block|{
name|List
argument_list|<
name|ObjEntity
argument_list|>
name|selected
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|selectedEntities
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Object
name|classObj
range|:
name|classes
control|)
block|{
if|if
condition|(
name|classObj
operator|instanceof
name|ObjEntity
operator|&&
name|selectedEntities
operator|.
name|contains
argument_list|(
operator|(
operator|(
name|ObjEntity
operator|)
name|classObj
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|selected
operator|.
name|add
argument_list|(
operator|(
operator|(
name|ObjEntity
operator|)
name|classObj
operator|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|selected
return|;
block|}
specifier|public
name|int
name|getSelectedEntitiesSize
parameter_list|()
block|{
return|return
name|selectedEntities
operator|.
name|size
argument_list|()
return|;
block|}
specifier|public
name|int
name|getSelectedEmbeddablesSize
parameter_list|()
block|{
return|return
name|selectedEmbeddables
operator|.
name|size
argument_list|()
return|;
block|}
comment|/**      * Returns the first encountered validation problem for an antity matching the name or      * null if the entity is valid or the entity is not present.      */
specifier|public
name|String
name|getProblem
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
name|String
name|name
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|obj
operator|instanceof
name|ObjEntity
condition|)
block|{
name|name
operator|=
operator|(
operator|(
name|ObjEntity
operator|)
name|obj
operator|)
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
if|else if
condition|(
name|obj
operator|instanceof
name|Embeddable
condition|)
block|{
name|name
operator|=
operator|(
operator|(
name|Embeddable
operator|)
name|obj
operator|)
operator|.
name|getClassName
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|validation
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|List
name|failures
init|=
name|validation
operator|.
name|getFailures
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|failures
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
operator|(
operator|(
name|ValidationFailure
operator|)
name|failures
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|)
operator|.
name|getDescription
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|isSelected
parameter_list|()
block|{
if|if
condition|(
name|currentClass
operator|instanceof
name|ObjEntity
condition|)
block|{
return|return
name|selectedEntities
operator|.
name|contains
argument_list|(
operator|(
operator|(
name|ObjEntity
operator|)
name|currentClass
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
if|if
condition|(
name|currentClass
operator|instanceof
name|Embeddable
condition|)
block|{
return|return
name|selectedEmbeddables
operator|.
name|contains
argument_list|(
operator|(
operator|(
name|Embeddable
operator|)
name|currentClass
operator|)
operator|.
name|getClassName
argument_list|()
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
specifier|public
name|void
name|setSelected
parameter_list|(
name|boolean
name|selectedFlag
parameter_list|)
block|{
if|if
condition|(
name|currentClass
operator|==
literal|null
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|currentClass
operator|instanceof
name|ObjEntity
condition|)
block|{
if|if
condition|(
name|selectedFlag
condition|)
block|{
if|if
condition|(
name|selectedEntities
operator|.
name|add
argument_list|(
operator|(
operator|(
name|ObjEntity
operator|)
name|currentClass
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|firePropertyChange
argument_list|(
name|SELECTED_PROPERTY
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|selectedEntities
operator|.
name|remove
argument_list|(
operator|(
operator|(
name|ObjEntity
operator|)
name|currentClass
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|firePropertyChange
argument_list|(
name|SELECTED_PROPERTY
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|currentClass
operator|instanceof
name|Embeddable
condition|)
block|{
if|if
condition|(
name|selectedFlag
condition|)
block|{
if|if
condition|(
name|selectedEmbeddables
operator|.
name|add
argument_list|(
operator|(
operator|(
name|Embeddable
operator|)
name|currentClass
operator|)
operator|.
name|getClassName
argument_list|()
argument_list|)
condition|)
block|{
name|firePropertyChange
argument_list|(
name|SELECTED_PROPERTY
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|selectedEmbeddables
operator|.
name|remove
argument_list|(
operator|(
operator|(
name|Embeddable
operator|)
name|currentClass
operator|)
operator|.
name|getClassName
argument_list|()
argument_list|)
condition|)
block|{
name|firePropertyChange
argument_list|(
name|SELECTED_PROPERTY
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|public
name|Object
name|getCurrentClass
parameter_list|()
block|{
return|return
name|currentClass
return|;
block|}
specifier|public
name|void
name|setCurrentClass
parameter_list|(
name|Object
name|currentClass
parameter_list|)
block|{
name|this
operator|.
name|currentClass
operator|=
name|currentClass
expr_stmt|;
block|}
specifier|public
name|Collection
argument_list|<
name|DataMap
argument_list|>
name|getDataMaps
parameter_list|()
block|{
return|return
name|dataMaps
return|;
block|}
specifier|public
name|JLabel
name|getItemName
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
name|String
name|className
decl_stmt|;
name|Icon
name|icon
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|obj
operator|instanceof
name|Embeddable
condition|)
block|{
name|className
operator|=
operator|(
operator|(
name|Embeddable
operator|)
name|obj
operator|)
operator|.
name|getClassName
argument_list|()
expr_stmt|;
name|icon
operator|=
name|CellRenderers
operator|.
name|iconForObject
argument_list|(
operator|new
name|Embeddable
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|className
operator|=
operator|(
operator|(
name|ObjEntity
operator|)
name|obj
operator|)
operator|.
name|getName
argument_list|()
expr_stmt|;
name|icon
operator|=
name|CellRenderers
operator|.
name|iconForObject
argument_list|(
operator|new
name|ObjEntity
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|JLabel
name|labelIcon
init|=
operator|new
name|JLabel
argument_list|()
decl_stmt|;
name|labelIcon
operator|.
name|setIcon
argument_list|(
name|icon
argument_list|)
expr_stmt|;
name|labelIcon
operator|.
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|labelIcon
operator|.
name|setText
argument_list|(
name|className
argument_list|)
expr_stmt|;
return|return
name|labelIcon
return|;
block|}
block|}
end_class

end_unit

