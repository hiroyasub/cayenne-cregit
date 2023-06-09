begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
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
operator|.
name|translator
operator|.
name|select
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Types
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|EnumMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
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
name|sqlbuilder
operator|.
name|sqltree
operator|.
name|ChildProcessor
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
name|sqlbuilder
operator|.
name|sqltree
operator|.
name|ColumnNode
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
name|sqlbuilder
operator|.
name|sqltree
operator|.
name|FunctionNode
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
name|sqlbuilder
operator|.
name|sqltree
operator|.
name|Node
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
name|sqlbuilder
operator|.
name|sqltree
operator|.
name|NodeType
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
name|sqlbuilder
operator|.
name|sqltree
operator|.
name|PerAttributeChildProcessor
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
name|sqlbuilder
operator|.
name|sqltree
operator|.
name|SQLTreeProcessor
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
name|sqlbuilder
operator|.
name|sqltree
operator|.
name|SimpleNodeTreeVisitor
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
name|sqlbuilder
operator|.
name|sqltree
operator|.
name|ValueNode
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
comment|/**  * @since 4.2  */
end_comment

begin_class
specifier|public
class|class
name|TypeAwareSQLTreeProcessor
extends|extends
name|SimpleNodeTreeVisitor
implements|implements
name|SQLTreeProcessor
block|{
specifier|protected
specifier|static
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|DEFAULT_TYPE
init|=
name|DefaultColumnTypeMarker
operator|.
name|class
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|String
name|DEFAULT_TYPE_NAME
init|=
name|DEFAULT_TYPE
operator|.
name|getName
argument_list|()
decl_stmt|;
specifier|protected
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|ChildProcessor
argument_list|<
name|ColumnNode
argument_list|>
argument_list|>
name|byColumnTypeProcessors
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|protected
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|ChildProcessor
argument_list|<
name|ValueNode
argument_list|>
argument_list|>
name|byValueTypeProcessors
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|protected
specifier|final
name|Map
argument_list|<
name|NodeType
argument_list|,
name|ChildProcessor
argument_list|<
name|Node
argument_list|>
argument_list|>
name|byNodeTypeProcessors
init|=
operator|new
name|EnumMap
argument_list|<>
argument_list|(
name|NodeType
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|TypeAwareSQLTreeProcessor
parameter_list|()
block|{
name|registerProcessor
argument_list|(
name|NodeType
operator|.
name|COLUMN
argument_list|,
operator|new
name|PerAttributeChildProcessor
argument_list|<>
argument_list|(
name|this
operator|::
name|getColumnAttribute
argument_list|,
name|this
operator|::
name|getColumnProcessor
argument_list|)
argument_list|)
expr_stmt|;
name|registerProcessor
argument_list|(
name|NodeType
operator|.
name|VALUE
argument_list|,
operator|new
name|PerAttributeChildProcessor
argument_list|<>
argument_list|(
name|this
operator|::
name|getValueAttribute
argument_list|,
name|this
operator|::
name|getValueProcessor
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Node
name|process
parameter_list|(
name|Node
name|node
parameter_list|)
block|{
name|node
operator|.
name|visit
argument_list|(
name|this
argument_list|)
expr_stmt|;
return|return
name|node
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|,
literal|"rawtypes"
block|}
argument_list|)
specifier|protected
name|void
name|registerProcessor
parameter_list|(
name|NodeType
name|nodeType
parameter_list|,
name|ChildProcessor
name|childProcessor
parameter_list|)
block|{
name|byNodeTypeProcessors
operator|.
name|put
argument_list|(
name|nodeType
argument_list|,
name|childProcessor
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|,
literal|"rawtypes"
block|}
argument_list|)
specifier|protected
name|void
name|registerColumnProcessor
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|columnType
parameter_list|,
name|ChildProcessor
name|childProcessor
parameter_list|)
block|{
name|byColumnTypeProcessors
operator|.
name|put
argument_list|(
name|columnType
operator|.
name|getName
argument_list|()
argument_list|,
name|childProcessor
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|,
literal|"rawtypes"
block|}
argument_list|)
specifier|protected
name|void
name|registerValueProcessor
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|columnType
parameter_list|,
name|ChildProcessor
name|childProcessor
parameter_list|)
block|{
name|byValueTypeProcessors
operator|.
name|put
argument_list|(
name|columnType
operator|.
name|getName
argument_list|()
argument_list|,
name|childProcessor
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|Optional
argument_list|<
name|Node
argument_list|>
name|defaultProcess
parameter_list|(
name|Node
name|parent
parameter_list|,
name|Node
name|child
parameter_list|,
name|int
name|index
parameter_list|)
block|{
return|return
name|Optional
operator|.
name|empty
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|onChildNodeStart
parameter_list|(
name|Node
name|parent
parameter_list|,
name|Node
name|child
parameter_list|,
name|int
name|index
parameter_list|,
name|boolean
name|hasMore
parameter_list|)
block|{
name|byNodeTypeProcessors
operator|.
name|getOrDefault
argument_list|(
name|child
operator|.
name|getType
argument_list|()
argument_list|,
name|this
operator|::
name|defaultProcess
argument_list|)
operator|.
name|process
argument_list|(
name|parent
argument_list|,
name|child
argument_list|,
name|index
argument_list|)
operator|.
name|ifPresent
argument_list|(
name|node
lambda|->
name|replaceChild
argument_list|(
name|parent
argument_list|,
name|index
argument_list|,
name|node
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
specifier|protected
name|DbAttribute
name|getColumnAttribute
parameter_list|(
name|ColumnNode
name|node
parameter_list|)
block|{
name|DbAttribute
name|attribute
init|=
name|node
operator|.
name|getAttribute
argument_list|()
decl_stmt|;
if|if
condition|(
name|attribute
operator|.
name|getType
argument_list|()
operator|==
name|Types
operator|.
name|OTHER
operator|&&
name|node
operator|.
name|getParent
argument_list|()
operator|!=
literal|null
operator|&&
name|node
operator|.
name|getParent
argument_list|()
operator|.
name|getType
argument_list|()
operator|==
name|NodeType
operator|.
name|RESULT
condition|)
block|{
return|return
name|attribute
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|protected
name|ChildProcessor
argument_list|<
name|ColumnNode
argument_list|>
name|getColumnProcessor
parameter_list|(
name|DbAttribute
name|attr
parameter_list|)
block|{
name|String
name|type
init|=
name|getObjAttributeFor
argument_list|(
name|attr
argument_list|)
operator|.
name|map
argument_list|(
name|ObjAttribute
operator|::
name|getType
argument_list|)
operator|.
name|orElse
argument_list|(
name|DEFAULT_TYPE_NAME
argument_list|)
decl_stmt|;
return|return
name|byColumnTypeProcessors
operator|.
name|getOrDefault
argument_list|(
name|type
argument_list|,
name|this
operator|::
name|defaultProcess
argument_list|)
return|;
block|}
specifier|protected
name|DbAttribute
name|getValueAttribute
parameter_list|(
name|ValueNode
name|node
parameter_list|)
block|{
name|DbAttribute
name|attribute
init|=
name|node
operator|.
name|getAttribute
argument_list|()
decl_stmt|;
if|if
condition|(
name|attribute
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|attribute
operator|.
name|getType
argument_list|()
operator|==
name|Types
operator|.
name|OTHER
operator|&&
name|node
operator|.
name|getParent
argument_list|()
operator|!=
literal|null
operator|&&
operator|(
name|node
operator|.
name|getParent
argument_list|()
operator|.
name|getType
argument_list|()
operator|==
name|NodeType
operator|.
name|EQUALITY
operator|||
name|node
operator|.
name|getParent
argument_list|()
operator|.
name|getType
argument_list|()
operator|==
name|NodeType
operator|.
name|INSERT_VALUES
operator|)
condition|)
block|{
return|return
name|attribute
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|protected
name|ChildProcessor
argument_list|<
name|ValueNode
argument_list|>
name|getValueProcessor
parameter_list|(
name|DbAttribute
name|attr
parameter_list|)
block|{
name|String
name|type
init|=
name|getObjAttributeFor
argument_list|(
name|attr
argument_list|)
operator|.
name|map
argument_list|(
name|ObjAttribute
operator|::
name|getType
argument_list|)
operator|.
name|orElse
argument_list|(
name|DEFAULT_TYPE_NAME
argument_list|)
decl_stmt|;
return|return
name|byValueTypeProcessors
operator|.
name|getOrDefault
argument_list|(
name|type
argument_list|,
name|this
operator|::
name|defaultProcess
argument_list|)
return|;
block|}
specifier|protected
specifier|static
name|void
name|replaceChild
parameter_list|(
name|Node
name|parent
parameter_list|,
name|int
name|index
parameter_list|,
name|Node
name|newChild
parameter_list|)
block|{
name|Node
name|oldChild
init|=
name|parent
operator|.
name|getChild
argument_list|(
name|index
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|oldChild
operator|.
name|getChildrenCount
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|newChild
operator|.
name|addChild
argument_list|(
name|oldChild
operator|.
name|getChild
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|parent
operator|.
name|replaceChild
argument_list|(
name|index
argument_list|,
name|newChild
argument_list|)
expr_stmt|;
block|}
specifier|protected
specifier|static
name|Node
name|wrapInFunction
parameter_list|(
name|Node
name|node
parameter_list|,
name|String
name|function
parameter_list|)
block|{
name|FunctionNode
name|functionNode
init|=
operator|new
name|FunctionNode
argument_list|(
name|function
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|functionNode
operator|.
name|addChild
argument_list|(
name|node
argument_list|)
expr_stmt|;
return|return
name|functionNode
return|;
block|}
specifier|protected
specifier|static
name|Optional
argument_list|<
name|ObjAttribute
argument_list|>
name|getObjAttributeFor
parameter_list|(
name|DbAttribute
name|dbAttribute
parameter_list|)
block|{
if|if
condition|(
name|dbAttribute
operator|==
literal|null
condition|)
block|{
return|return
name|Optional
operator|.
name|empty
argument_list|()
return|;
block|}
name|DbEntity
name|dbEntity
init|=
name|dbAttribute
operator|.
name|getEntity
argument_list|()
decl_stmt|;
for|for
control|(
name|ObjEntity
name|objEntity
range|:
name|dbEntity
operator|.
name|getDataMap
argument_list|()
operator|.
name|getObjEntities
argument_list|()
control|)
block|{
name|ObjAttribute
name|objAttribute
init|=
name|objEntity
operator|.
name|getAttributeForDbAttribute
argument_list|(
name|dbAttribute
argument_list|)
decl_stmt|;
if|if
condition|(
name|objAttribute
operator|!=
literal|null
condition|)
block|{
return|return
name|Optional
operator|.
name|of
argument_list|(
name|objAttribute
argument_list|)
return|;
block|}
block|}
return|return
name|Optional
operator|.
name|empty
argument_list|()
return|;
block|}
specifier|private
specifier|static
class|class
name|DefaultColumnTypeMarker
block|{     }
block|}
end_class

end_unit

