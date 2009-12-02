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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|geom
operator|.
name|Rectangle2D
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Hashtable
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
name|jgraph
operator|.
name|graph
operator|.
name|GraphConstants
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
name|SAXException
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
name|helpers
operator|.
name|DefaultHandler
import|;
end_import

begin_comment
comment|/**  * Class to load graph from XML  */
end_comment

begin_class
specifier|public
class|class
name|GraphLoader
extends|extends
name|DefaultHandler
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
comment|/**      * Map graphs will be loaded into      */
name|GraphMap
name|map
decl_stmt|;
comment|/**      * Current builder      */
name|GraphBuilder
name|builder
decl_stmt|;
comment|/**      * Changed properties for every builder      */
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
decl_stmt|;
specifier|public
name|GraphLoader
parameter_list|(
name|GraphMap
name|map
parameter_list|)
block|{
name|this
operator|.
name|map
operator|=
name|map
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|startElement
parameter_list|(
name|String
name|uri
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
throws|throws
name|SAXException
block|{
if|if
condition|(
name|GRAPH_TAG
operator|.
name|equalsIgnoreCase
argument_list|(
name|localName
argument_list|)
condition|)
block|{
name|String
name|type
init|=
name|attributes
operator|.
name|getValue
argument_list|(
literal|""
argument_list|,
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
name|GraphType
name|graphType
init|=
name|GraphType
operator|.
name|valueOf
argument_list|(
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|graphType
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|SAXException
argument_list|(
literal|"Graph type "
operator|+
name|type
operator|+
literal|" not supported"
argument_list|)
throw|;
block|}
name|builder
operator|=
name|map
operator|.
name|createGraphBuilder
argument_list|(
name|graphType
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|builder
operator|.
name|getGraph
argument_list|()
operator|.
name|setScale
argument_list|(
name|getAsDouble
argument_list|(
name|attributes
argument_list|,
literal|"scale"
argument_list|)
argument_list|)
expr_stmt|;
name|propertiesMap
operator|=
operator|new
name|Hashtable
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
argument_list|()
expr_stmt|;
block|}
if|else if
condition|(
name|ENTITY_TAG
operator|.
name|equalsIgnoreCase
argument_list|(
name|localName
argument_list|)
condition|)
block|{
name|String
name|name
init|=
name|attributes
operator|.
name|getValue
argument_list|(
literal|""
argument_list|,
literal|"name"
argument_list|)
decl_stmt|;
name|DefaultGraphCell
name|cell
init|=
name|builder
operator|.
name|getEntityCell
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|cell
operator|!=
literal|null
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|props
init|=
operator|new
name|Hashtable
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|GraphConstants
operator|.
name|setBounds
argument_list|(
name|props
argument_list|,
operator|new
name|Rectangle2D
operator|.
name|Double
argument_list|(
name|getAsDouble
argument_list|(
name|attributes
argument_list|,
literal|"x"
argument_list|)
argument_list|,
name|getAsDouble
argument_list|(
name|attributes
argument_list|,
literal|"y"
argument_list|)
argument_list|,
name|getAsDouble
argument_list|(
name|attributes
argument_list|,
literal|"width"
argument_list|)
argument_list|,
name|getAsDouble
argument_list|(
name|attributes
argument_list|,
literal|"height"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|propertiesMap
operator|.
name|put
argument_list|(
name|cell
argument_list|,
name|props
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|endElement
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|localName
parameter_list|,
name|String
name|qName
parameter_list|)
throws|throws
name|SAXException
block|{
if|if
condition|(
name|GRAPH_TAG
operator|.
name|equalsIgnoreCase
argument_list|(
name|localName
argument_list|)
condition|)
block|{
comment|//apply changes
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
block|}
specifier|private
name|double
name|getAsDouble
parameter_list|(
name|Attributes
name|atts
parameter_list|,
name|String
name|key
parameter_list|)
block|{
return|return
name|Double
operator|.
name|valueOf
argument_list|(
name|atts
operator|.
name|getValue
argument_list|(
literal|""
argument_list|,
name|key
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

