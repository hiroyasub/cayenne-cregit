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
name|editor
operator|.
name|dbimport
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
name|dbsync
operator|.
name|reverse
operator|.
name|dbimport
operator|.
name|Catalog
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
name|dbsync
operator|.
name|reverse
operator|.
name|dbimport
operator|.
name|ExcludeColumn
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
name|dbsync
operator|.
name|reverse
operator|.
name|dbimport
operator|.
name|ExcludeProcedure
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
name|dbsync
operator|.
name|reverse
operator|.
name|dbimport
operator|.
name|ExcludeTable
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
name|dbsync
operator|.
name|reverse
operator|.
name|dbimport
operator|.
name|IncludeColumn
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
name|dbsync
operator|.
name|reverse
operator|.
name|dbimport
operator|.
name|IncludeProcedure
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
name|dbsync
operator|.
name|reverse
operator|.
name|dbimport
operator|.
name|IncludeTable
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
name|dbsync
operator|.
name|reverse
operator|.
name|dbimport
operator|.
name|Schema
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
name|dialog
operator|.
name|db
operator|.
name|load
operator|.
name|DbImportTreeNode
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
name|ModelerUtil
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
name|JTree
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
comment|/**  * @since 4.1  */
end_comment

begin_class
specifier|public
class|class
name|DbImportTreeCellRenderer
extends|extends
name|DefaultTreeCellRenderer
block|{
specifier|protected
name|DbImportTreeNode
name|node
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|Class
argument_list|,
name|String
argument_list|>
name|icons
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|Class
argument_list|,
name|String
argument_list|>
name|transferableTreeIcons
decl_stmt|;
specifier|public
name|DbImportTreeCellRenderer
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
name|initIcons
argument_list|()
expr_stmt|;
name|initTrasferableTreeIcons
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|initTrasferableTreeIcons
parameter_list|()
block|{
name|transferableTreeIcons
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|transferableTreeIcons
operator|.
name|put
argument_list|(
name|Catalog
operator|.
name|class
argument_list|,
literal|"icon-dbi-catalog.png"
argument_list|)
expr_stmt|;
name|transferableTreeIcons
operator|.
name|put
argument_list|(
name|Schema
operator|.
name|class
argument_list|,
literal|"icon-dbi-schema.png"
argument_list|)
expr_stmt|;
name|transferableTreeIcons
operator|.
name|put
argument_list|(
name|IncludeTable
operator|.
name|class
argument_list|,
literal|"icon-dbentity.png"
argument_list|)
expr_stmt|;
name|transferableTreeIcons
operator|.
name|put
argument_list|(
name|IncludeProcedure
operator|.
name|class
argument_list|,
literal|"icon-stored-procedure.png"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|initIcons
parameter_list|()
block|{
name|icons
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|icons
operator|.
name|put
argument_list|(
name|Catalog
operator|.
name|class
argument_list|,
literal|"icon-dbi-catalog.png"
argument_list|)
expr_stmt|;
name|icons
operator|.
name|put
argument_list|(
name|Schema
operator|.
name|class
argument_list|,
literal|"icon-dbi-schema.png"
argument_list|)
expr_stmt|;
name|icons
operator|.
name|put
argument_list|(
name|IncludeTable
operator|.
name|class
argument_list|,
literal|"icon-dbi-includeTable.png"
argument_list|)
expr_stmt|;
name|icons
operator|.
name|put
argument_list|(
name|ExcludeTable
operator|.
name|class
argument_list|,
literal|"icon-dbi-excludeTable.png"
argument_list|)
expr_stmt|;
name|icons
operator|.
name|put
argument_list|(
name|IncludeColumn
operator|.
name|class
argument_list|,
literal|"icon-dbi-includeColumn.png"
argument_list|)
expr_stmt|;
name|icons
operator|.
name|put
argument_list|(
name|ExcludeColumn
operator|.
name|class
argument_list|,
literal|"icon-dbi-excludeColumn.png"
argument_list|)
expr_stmt|;
name|icons
operator|.
name|put
argument_list|(
name|IncludeProcedure
operator|.
name|class
argument_list|,
literal|"icon-dbi-includeProcedure.png"
argument_list|)
expr_stmt|;
name|icons
operator|.
name|put
argument_list|(
name|ExcludeProcedure
operator|.
name|class
argument_list|,
literal|"icon-dbi-excludeProcedure.png"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|ImageIcon
name|getIconByNodeType
parameter_list|(
name|Class
name|nodeClass
parameter_list|,
name|boolean
name|isTransferable
parameter_list|)
block|{
name|String
name|iconName
init|=
operator|!
name|isTransferable
condition|?
name|icons
operator|.
name|get
argument_list|(
name|nodeClass
argument_list|)
else|:
name|transferableTreeIcons
operator|.
name|get
argument_list|(
name|nodeClass
argument_list|)
decl_stmt|;
if|if
condition|(
name|iconName
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|ModelerUtil
operator|.
name|buildIcon
argument_list|(
name|iconName
argument_list|)
return|;
block|}
annotation|@
name|Override
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
name|node
operator|=
operator|(
name|DbImportTreeNode
operator|)
name|value
expr_stmt|;
name|setIcon
argument_list|(
name|getIconByNodeType
argument_list|(
name|node
operator|.
name|getUserObject
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|,
operator|(
operator|(
name|DbImportTree
operator|)
name|tree
operator|)
operator|.
name|isTransferable
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
end_class

end_unit

