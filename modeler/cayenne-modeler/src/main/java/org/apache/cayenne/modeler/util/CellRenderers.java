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
name|util
package|;
end_package

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
name|javax
operator|.
name|swing
operator|.
name|DefaultListCellRenderer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|ImageIcon
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JList
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JTable
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JTree
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|ListCellRenderer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|table
operator|.
name|DefaultTableCellRenderer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|table
operator|.
name|TableCellRenderer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|tree
operator|.
name|DefaultMutableTreeNode
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|tree
operator|.
name|DefaultTreeCellRenderer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|tree
operator|.
name|TreeCellRenderer
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
name|Attribute
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
name|Entity
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
name|MappingNamespace
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
name|Relationship
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
name|ProjectController
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
name|CayenneMapEntry
import|;
end_import

begin_comment
comment|/**  * Utility class that serves as a factory for various project renderers.  *   * @since 1.1  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|CellRenderers
block|{
comment|// common icons
specifier|protected
specifier|static
name|ImageIcon
name|domainIcon
decl_stmt|;
specifier|protected
specifier|static
name|ImageIcon
name|nodeIcon
decl_stmt|;
specifier|protected
specifier|static
name|ImageIcon
name|mapIcon
decl_stmt|;
specifier|protected
specifier|static
name|ImageIcon
name|dbEntityIcon
decl_stmt|;
specifier|protected
specifier|static
name|ImageIcon
name|objEntityIcon
decl_stmt|;
specifier|protected
specifier|static
name|ImageIcon
name|relationshipIcon
decl_stmt|;
specifier|protected
specifier|static
name|ImageIcon
name|attributeIcon
decl_stmt|;
specifier|protected
specifier|static
name|ImageIcon
name|procedureIcon
decl_stmt|;
specifier|protected
specifier|static
name|ImageIcon
name|queryIcon
decl_stmt|;
specifier|protected
specifier|static
name|ImageIcon
name|embeddableIcon
decl_stmt|;
specifier|protected
specifier|static
name|ImageIcon
name|catalogIcon
decl_stmt|;
static|static
block|{
name|domainIcon
operator|=
name|ModelerUtil
operator|.
name|buildIcon
argument_list|(
literal|"icon-dom.gif"
argument_list|)
expr_stmt|;
name|nodeIcon
operator|=
name|ModelerUtil
operator|.
name|buildIcon
argument_list|(
literal|"icon-node.gif"
argument_list|)
expr_stmt|;
name|mapIcon
operator|=
name|ModelerUtil
operator|.
name|buildIcon
argument_list|(
literal|"icon-datamap.gif"
argument_list|)
expr_stmt|;
name|dbEntityIcon
operator|=
name|ModelerUtil
operator|.
name|buildIcon
argument_list|(
literal|"icon-dbentity.gif"
argument_list|)
expr_stmt|;
name|objEntityIcon
operator|=
name|ModelerUtil
operator|.
name|buildIcon
argument_list|(
literal|"icon-objentity.gif"
argument_list|)
expr_stmt|;
name|procedureIcon
operator|=
name|ModelerUtil
operator|.
name|buildIcon
argument_list|(
literal|"icon-stored-procedure.gif"
argument_list|)
expr_stmt|;
name|queryIcon
operator|=
name|ModelerUtil
operator|.
name|buildIcon
argument_list|(
literal|"icon-query.gif"
argument_list|)
expr_stmt|;
name|relationshipIcon
operator|=
name|ModelerUtil
operator|.
name|buildIcon
argument_list|(
literal|"icon-relationship.gif"
argument_list|)
expr_stmt|;
name|attributeIcon
operator|=
name|ModelerUtil
operator|.
name|buildIcon
argument_list|(
literal|"icon-attribute.gif"
argument_list|)
expr_stmt|;
name|embeddableIcon
operator|=
name|ModelerUtil
operator|.
name|buildIcon
argument_list|(
literal|"icon-embeddable.gif"
argument_list|)
expr_stmt|;
name|catalogIcon
operator|=
name|ModelerUtil
operator|.
name|buildIcon
argument_list|(
literal|"icon-open.gif"
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|ImageIcon
name|iconForObject
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
if|if
condition|(
name|object
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
name|object
operator|instanceof
name|DataChannelDescriptor
condition|)
block|{
return|return
name|domainIcon
return|;
block|}
if|else if
condition|(
name|object
operator|instanceof
name|DataNodeDescriptor
condition|)
block|{
return|return
name|nodeIcon
return|;
block|}
if|else if
condition|(
name|object
operator|instanceof
name|DataMap
condition|)
block|{
return|return
name|mapIcon
return|;
block|}
if|else if
condition|(
name|object
operator|instanceof
name|Entity
condition|)
block|{
name|Entity
name|entity
init|=
operator|(
name|Entity
operator|)
name|object
decl_stmt|;
if|if
condition|(
name|entity
operator|instanceof
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|map
operator|.
name|DbEntity
condition|)
block|{
return|return
name|dbEntityIcon
return|;
block|}
if|else if
condition|(
name|entity
operator|instanceof
name|ObjEntity
condition|)
block|{
return|return
name|objEntityIcon
return|;
block|}
block|}
if|else if
condition|(
name|object
operator|instanceof
name|Procedure
condition|)
block|{
return|return
name|procedureIcon
return|;
block|}
if|else if
condition|(
name|object
operator|instanceof
name|QueryDescriptor
condition|)
block|{
return|return
name|queryIcon
return|;
block|}
if|else if
condition|(
name|object
operator|instanceof
name|Relationship
condition|)
block|{
return|return
name|relationshipIcon
return|;
block|}
if|else if
condition|(
name|object
operator|instanceof
name|Attribute
condition|)
block|{
return|return
name|attributeIcon
return|;
block|}
if|else if
condition|(
name|object
operator|instanceof
name|Embeddable
condition|)
block|{
return|return
name|embeddableIcon
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Returns a TreeCellRenderer to display Cayenne project tree nodes with icons.      */
specifier|public
specifier|static
name|TreeCellRenderer
name|treeRenderer
parameter_list|()
block|{
return|return
operator|new
name|TreeRenderer
argument_list|()
return|;
block|}
comment|/**      * Returns a ListCellRenderer to display Cayenne project tree nodes without icons.      */
specifier|public
specifier|static
name|ListCellRenderer
argument_list|<
name|Object
argument_list|>
name|listRenderer
parameter_list|()
block|{
return|return
operator|new
name|ListRenderer
argument_list|(
literal|false
argument_list|)
return|;
block|}
comment|/**      * Returns a ListCellRenderer to display Cayenne project tree nodes with icons.      */
specifier|public
specifier|static
name|ListCellRenderer
argument_list|<
name|Object
argument_list|>
name|listRendererWithIcons
parameter_list|()
block|{
return|return
operator|new
name|ListRenderer
argument_list|(
literal|true
argument_list|)
return|;
block|}
comment|/**      * Returns a ListCellRenderer to display Cayenne project tree nodes with icons.      */
specifier|public
specifier|static
name|ListCellRenderer
argument_list|<
name|Object
argument_list|>
name|entityListRendererWithIcons
parameter_list|(
name|MappingNamespace
name|namespace
parameter_list|)
block|{
return|return
operator|new
name|EntityRenderer
argument_list|(
name|namespace
argument_list|)
return|;
block|}
comment|/**      * Returns a TableCellRenderer to display Cayenne project entities with icons in table.      */
specifier|public
specifier|static
name|TableCellRenderer
name|entityTableRendererWithIcons
parameter_list|(
name|ProjectController
name|mediator
parameter_list|)
block|{
return|return
operator|new
name|EntityTableRenderer
argument_list|(
name|mediator
argument_list|)
return|;
block|}
comment|/**      * Converts non-String Object used in renderers (currently CayenneMapEntry      * instances only) to String      *       * @param obj Object to be converted      */
specifier|public
specifier|static
name|String
name|asString
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
return|return
name|asString
argument_list|(
name|obj
argument_list|,
name|Application
operator|.
name|getInstance
argument_list|()
operator|.
comment|//none of these is suppeosed to be null
name|getFrameController
argument_list|()
operator|.
name|getProjectController
argument_list|()
operator|.
name|getCurrentDataMap
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Converts non-String Object used in renderers (currently CayenneMapEntry      * instances only) to String      *       * @param obj Object to be converted      * @param namespace the current namespace      */
specifier|public
specifier|static
name|String
name|asString
parameter_list|(
name|Object
name|obj
parameter_list|,
name|MappingNamespace
name|namespace
parameter_list|)
block|{
if|if
condition|(
name|obj
operator|instanceof
name|CayenneMapEntry
condition|)
block|{
name|CayenneMapEntry
name|mapObject
init|=
operator|(
name|CayenneMapEntry
operator|)
name|obj
decl_stmt|;
name|String
name|label
init|=
name|mapObject
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|mapObject
operator|instanceof
name|Entity
condition|)
block|{
name|Entity
name|entity
init|=
operator|(
name|Entity
operator|)
name|mapObject
decl_stmt|;
comment|// for different namespace display its name
name|DataMap
name|dataMap
init|=
name|entity
operator|.
name|getDataMap
argument_list|()
decl_stmt|;
if|if
condition|(
name|dataMap
operator|!=
literal|null
operator|&&
name|dataMap
operator|!=
name|namespace
condition|)
block|{
name|label
operator|+=
literal|" ("
operator|+
name|dataMap
operator|.
name|getName
argument_list|()
operator|+
literal|")"
expr_stmt|;
block|}
block|}
return|return
name|label
return|;
block|}
if|else if
condition|(
name|obj
operator|instanceof
name|DataMap
condition|)
block|{
return|return
operator|(
operator|(
name|DataMap
operator|)
name|obj
operator|)
operator|.
name|getName
argument_list|()
return|;
block|}
return|return
name|obj
operator|==
literal|null
condition|?
literal|null
else|:
name|String
operator|.
name|valueOf
argument_list|(
name|obj
argument_list|)
return|;
block|}
specifier|final
specifier|static
class|class
name|EntityRenderer
extends|extends
name|DefaultListCellRenderer
block|{
name|MappingNamespace
name|namespace
decl_stmt|;
name|EntityRenderer
parameter_list|(
name|MappingNamespace
name|namespace
parameter_list|)
block|{
name|this
operator|.
name|namespace
operator|=
name|namespace
expr_stmt|;
block|}
comment|/**           * Will trim the value to fit defined size.           */
specifier|public
name|Component
name|getListCellRendererComponent
parameter_list|(
name|JList
name|list
parameter_list|,
name|Object
name|value
parameter_list|,
name|int
name|index
parameter_list|,
name|boolean
name|isSelected
parameter_list|,
name|boolean
name|cellHasFocus
parameter_list|)
block|{
comment|// the sequence is important - call super with converted value,
comment|// then set an icon, and then return "this"
name|ImageIcon
name|icon
init|=
name|CellRenderers
operator|.
name|iconForObject
argument_list|(
name|value
argument_list|)
decl_stmt|;
name|value
operator|=
name|asString
argument_list|(
name|value
argument_list|,
name|namespace
argument_list|)
expr_stmt|;
name|super
operator|.
name|getListCellRendererComponent
argument_list|(
name|list
argument_list|,
name|value
argument_list|,
name|index
argument_list|,
name|isSelected
argument_list|,
name|cellHasFocus
argument_list|)
expr_stmt|;
name|setIcon
argument_list|(
name|icon
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
specifier|final
specifier|static
class|class
name|ListRenderer
extends|extends
name|DefaultListCellRenderer
block|{
name|boolean
name|showIcons
decl_stmt|;
name|ListRenderer
parameter_list|(
name|boolean
name|showIcons
parameter_list|)
block|{
name|this
operator|.
name|showIcons
operator|=
name|showIcons
expr_stmt|;
block|}
comment|/**           * Will trim the value to fit defined size.           */
specifier|public
name|Component
name|getListCellRendererComponent
parameter_list|(
name|JList
name|list
parameter_list|,
name|Object
name|value
parameter_list|,
name|int
name|index
parameter_list|,
name|boolean
name|isSelected
parameter_list|,
name|boolean
name|cellHasFocus
parameter_list|)
block|{
comment|// the sequence is important - call super with converted value,
comment|// then set an icon, and then return "this"
name|Object
name|renderedValue
init|=
name|ModelerUtil
operator|.
name|getObjectName
argument_list|(
name|value
argument_list|)
decl_stmt|;
if|if
condition|(
name|renderedValue
operator|==
literal|null
condition|)
block|{
comment|// render NULL as empty string
name|renderedValue
operator|=
literal|" "
expr_stmt|;
block|}
name|super
operator|.
name|getListCellRendererComponent
argument_list|(
name|list
argument_list|,
name|renderedValue
argument_list|,
name|index
argument_list|,
name|isSelected
argument_list|,
name|cellHasFocus
argument_list|)
expr_stmt|;
if|if
condition|(
name|showIcons
condition|)
block|{
name|setIcon
argument_list|(
name|iconForObject
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
block|}
specifier|final
specifier|static
class|class
name|TreeRenderer
extends|extends
name|DefaultTreeCellRenderer
block|{
specifier|public
name|Component
name|getTreeCellRendererComponent
parameter_list|(
name|JTree
name|tree
parameter_list|,
name|Object
name|value
parameter_list|,
name|boolean
name|sel
parameter_list|,
name|boolean
name|expanded
parameter_list|,
name|boolean
name|leaf
parameter_list|,
name|int
name|row
parameter_list|,
name|boolean
name|hasFocus
parameter_list|)
block|{
comment|// the sequence is important - call super,
comment|// then set an icon, and then return "this"
name|super
operator|.
name|getTreeCellRendererComponent
argument_list|(
name|tree
argument_list|,
name|value
argument_list|,
name|sel
argument_list|,
name|expanded
argument_list|,
name|leaf
argument_list|,
name|row
argument_list|,
name|hasFocus
argument_list|)
expr_stmt|;
name|DefaultMutableTreeNode
name|node
init|=
operator|(
name|DefaultMutableTreeNode
operator|)
name|value
decl_stmt|;
name|setIcon
argument_list|(
name|iconForObject
argument_list|(
name|node
operator|.
name|getUserObject
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
specifier|final
specifier|static
class|class
name|EntityTableRenderer
extends|extends
name|DefaultTableCellRenderer
block|{
specifier|private
name|ProjectController
name|mediator
decl_stmt|;
specifier|public
name|EntityTableRenderer
parameter_list|(
name|ProjectController
name|mediator
parameter_list|)
block|{
name|this
operator|.
name|mediator
operator|=
name|mediator
expr_stmt|;
block|}
specifier|public
name|Component
name|getTableCellRendererComponent
parameter_list|(
name|JTable
name|table
parameter_list|,
name|Object
name|value
parameter_list|,
name|boolean
name|isSelected
parameter_list|,
name|boolean
name|hasFocus
parameter_list|,
name|int
name|row
parameter_list|,
name|int
name|column
parameter_list|)
block|{
name|Object
name|oldValue
init|=
name|value
decl_stmt|;
name|value
operator|=
name|CellRenderers
operator|.
name|asString
argument_list|(
name|value
argument_list|,
name|mediator
operator|.
name|getCurrentDataMap
argument_list|()
argument_list|)
expr_stmt|;
name|super
operator|.
name|getTableCellRendererComponent
argument_list|(
name|table
argument_list|,
name|value
argument_list|,
name|isSelected
argument_list|,
name|hasFocus
argument_list|,
name|row
argument_list|,
name|column
argument_list|)
expr_stmt|;
name|setIcon
argument_list|(
name|CellRenderers
operator|.
name|iconForObject
argument_list|(
name|oldValue
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
block|}
end_class

end_unit

