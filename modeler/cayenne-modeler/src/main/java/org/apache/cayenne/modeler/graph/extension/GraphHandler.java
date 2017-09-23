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
name|graph
operator|.
name|extension
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
name|configuration
operator|.
name|xml
operator|.
name|NamespaceAwareNestedTagHandler
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
name|Application
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
name|graph
operator|.
name|GraphBuilder
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
name|graph
operator|.
name|GraphMap
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
name|graph
operator|.
name|GraphRegistry
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
name|graph
operator|.
name|GraphType
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

begin_import
import|import
name|org
operator|.
name|jgraph
operator|.
name|graph
operator|.
name|DefaultGraphCell
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|Attributes
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|ContentHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|SAXException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|undo
operator|.
name|UndoableEdit
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

begin_comment
comment|/**  * Class to load graph from XML  */
end_comment

begin_class
class|class
name|GraphHandler
extends|extends
name|NamespaceAwareNestedTagHandler
block|{
specifier|static
specifier|final
name|String
name|GRAPH_TAG
init|=
literal|"graph"
decl_stmt|;
specifier|static
specifier|final
name|String
name|ENTITY_TAG
init|=
literal|"entity"
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
argument_list|>
name|propertiesMap
decl_stmt|;
name|GraphType
name|graphType
decl_stmt|;
name|double
name|scale
decl_stmt|;
specifier|public
name|GraphHandler
parameter_list|(
name|NamespaceAwareNestedTagHandler
name|parent
parameter_list|,
specifier|final
name|Application
name|application
parameter_list|)
block|{
name|super
argument_list|(
name|parent
argument_list|)
expr_stmt|;
name|loaderContext
operator|.
name|addDataChannelListener
argument_list|(
name|dataChannelDescriptor
lambda|->
block|{
name|GraphRegistry
name|registry
init|=
name|application
operator|.
name|getMetaData
argument_list|()
operator|.
name|get
argument_list|(
name|dataChannelDescriptor
argument_list|,
name|GraphRegistry
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|registry
operator|==
literal|null
condition|)
block|{
name|registry
operator|=
operator|new
name|GraphRegistry
argument_list|()
expr_stmt|;
name|application
operator|.
name|getMetaData
argument_list|()
operator|.
name|add
argument_list|(
name|dataChannelDescriptor
argument_list|,
name|registry
argument_list|)
expr_stmt|;
block|}
name|GraphMap
name|map
init|=
name|registry
operator|.
name|getGraphMap
argument_list|(
name|dataChannelDescriptor
argument_list|)
decl_stmt|;
comment|//apply changes
name|GraphBuilder
name|builder
init|=
name|map
operator|.
name|createGraphBuilder
argument_list|(
name|graphType
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|builder
operator|.
name|getGraph
argument_list|()
operator|.
name|setScale
argument_list|(
name|scale
argument_list|)
expr_stmt|;
comment|// lookup
name|Map
argument_list|<
name|DefaultGraphCell
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
argument_list|>
name|propertiesMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
argument_list|>
name|entry
range|:
name|GraphHandler
operator|.
name|this
operator|.
name|propertiesMap
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|DefaultGraphCell
name|cell
init|=
name|builder
operator|.
name|getEntityCell
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
name|propertiesMap
operator|.
name|put
argument_list|(
name|cell
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|builder
operator|.
name|getGraph
argument_list|()
operator|.
name|getGraphLayoutCache
argument_list|()
operator|.
name|getModel
argument_list|()
operator|.
name|removeUndoableEditListener
argument_list|(
name|builder
argument_list|)
expr_stmt|;
name|builder
operator|.
name|getGraph
argument_list|()
operator|.
name|getGraphLayoutCache
argument_list|()
operator|.
name|edit
argument_list|(
name|propertiesMap
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
operator|new
name|UndoableEdit
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|builder
operator|.
name|getGraph
argument_list|()
operator|.
name|getGraphLayoutCache
argument_list|()
operator|.
name|getModel
argument_list|()
operator|.
name|addUndoableEditListener
argument_list|(
name|builder
argument_list|)
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|boolean
name|processElement
parameter_list|(
name|String
name|namespaceURI
parameter_list|,
name|String
name|localName
parameter_list|,
name|Attributes
name|attributes
parameter_list|)
throws|throws
name|SAXException
block|{
switch|switch
condition|(
name|localName
condition|)
block|{
case|case
name|GRAPH_TAG
case|:
name|String
name|type
init|=
name|attributes
operator|.
name|getValue
argument_list|(
literal|"type"
argument_list|)
decl_stmt|;
if|if
condition|(
name|Util
operator|.
name|isEmptyString
argument_list|(
name|type
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|SAXException
argument_list|(
literal|"Graph type not specified"
argument_list|)
throw|;
block|}
name|graphType
operator|=
name|GraphType
operator|.
name|valueOf
argument_list|(
name|type
argument_list|)
expr_stmt|;
name|scale
operator|=
name|Double
operator|.
name|valueOf
argument_list|(
name|attributes
operator|.
name|getValue
argument_list|(
literal|"scale"
argument_list|)
argument_list|)
expr_stmt|;
name|propertiesMap
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|protected
name|ContentHandler
name|createChildTagHandler
parameter_list|(
name|String
name|namespaceURI
parameter_list|,
name|String
name|localName
parameter_list|,
name|String
name|qName
parameter_list|,
name|Attributes
name|attributes
parameter_list|)
block|{
switch|switch
condition|(
name|localName
condition|)
block|{
case|case
name|ENTITY_TAG
case|:
return|return
operator|new
name|EntityHandler
argument_list|(
name|this
argument_list|)
return|;
block|}
return|return
name|super
operator|.
name|createChildTagHandler
argument_list|(
name|namespaceURI
argument_list|,
name|localName
argument_list|,
name|qName
argument_list|,
name|attributes
argument_list|)
return|;
block|}
block|}
end_class

end_unit

