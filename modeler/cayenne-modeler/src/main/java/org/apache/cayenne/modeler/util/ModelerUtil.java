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
name|Dimension
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Point
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Window
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|math
operator|.
name|BigInteger
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Calendar
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
name|ResourceBundle
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
name|javax
operator|.
name|swing
operator|.
name|Action
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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|access
operator|.
name|types
operator|.
name|ExtendedTypeMap
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
name|ModelerConstants
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
name|modeler
operator|.
name|action
operator|.
name|ActionManager
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
name|action
operator|.
name|MultipleObjectsAction
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
name|reflect
operator|.
name|PropertyUtils
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
comment|/**  * Various unorganized utility methods used by CayenneModeler.  *   */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|ModelerUtil
block|{
comment|/**      * Returns the "name" property of the object.      *       * @since 1.1      */
specifier|public
specifier|static
name|String
name|getObjectName
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
if|else if
condition|(
name|object
operator|instanceof
name|CayenneMapEntry
condition|)
block|{
return|return
operator|(
operator|(
name|CayenneMapEntry
operator|)
name|object
operator|)
operator|.
name|getName
argument_list|()
return|;
block|}
if|else if
condition|(
name|object
operator|instanceof
name|String
condition|)
block|{
return|return
operator|(
name|String
operator|)
name|object
return|;
block|}
else|else
block|{
try|try
block|{
comment|// use reflection
return|return
operator|(
name|String
operator|)
name|PropertyUtils
operator|.
name|getProperty
argument_list|(
name|object
argument_list|,
literal|"name"
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
comment|/**      * Returns an icon, building it from an image file located at the shared resources      * folder for the modeler.      */
specifier|public
specifier|static
name|ImageIcon
name|buildIcon
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|ClassLoader
name|cl
init|=
name|ModelerUtil
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
decl_stmt|;
name|URL
name|url
init|=
name|cl
operator|.
name|getResource
argument_list|(
name|ModelerConstants
operator|.
name|RESOURCE_PATH
operator|+
name|path
argument_list|)
decl_stmt|;
return|return
operator|new
name|ImageIcon
argument_list|(
name|url
argument_list|)
return|;
block|}
comment|/**      * Returns array of db attribute names for DbEntity mapped to current ObjEntity.      */
specifier|public
specifier|static
name|Collection
argument_list|<
name|String
argument_list|>
name|getDbAttributeNames
parameter_list|(
name|ProjectController
name|mediator
parameter_list|,
name|DbEntity
name|entity
parameter_list|)
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|keys
init|=
name|entity
operator|.
name|getAttributeMap
argument_list|()
operator|.
name|keySet
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
name|keys
operator|.
name|size
argument_list|()
operator|+
literal|1
argument_list|)
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|list
operator|.
name|addAll
argument_list|(
name|keys
argument_list|)
expr_stmt|;
return|return
name|list
return|;
block|}
specifier|public
specifier|static
name|String
index|[]
name|getRegisteredTypeNames
parameter_list|()
block|{
name|String
index|[]
name|explicitList
init|=
operator|new
name|ExtendedTypeMap
argument_list|()
operator|.
name|getRegisteredTypeNames
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|nonPrimitives
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|explicitList
argument_list|)
argument_list|)
decl_stmt|;
comment|// add types that are not mapped explicitly, but nevertheless supported by Cayenne
name|nonPrimitives
operator|.
name|add
argument_list|(
name|Calendar
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|nonPrimitives
operator|.
name|add
argument_list|(
name|BigInteger
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|nonPrimitives
operator|.
name|add
argument_list|(
name|Serializable
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|nonPrimitives
operator|.
name|add
argument_list|(
name|Character
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|nonPrimitives
operator|.
name|add
argument_list|(
literal|"char[]"
argument_list|)
expr_stmt|;
name|nonPrimitives
operator|.
name|add
argument_list|(
literal|"java.lang.Character[]"
argument_list|)
expr_stmt|;
name|nonPrimitives
operator|.
name|add
argument_list|(
literal|"java.lang.Byte[]"
argument_list|)
expr_stmt|;
name|nonPrimitives
operator|.
name|add
argument_list|(
literal|"java.util.Date"
argument_list|)
expr_stmt|;
name|nonPrimitives
operator|.
name|remove
argument_list|(
name|Void
operator|.
name|TYPE
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|String
index|[]
name|nonPrimitivesNames
init|=
operator|new
name|String
index|[
name|nonPrimitives
operator|.
name|size
argument_list|()
index|]
decl_stmt|;
name|nonPrimitives
operator|.
name|toArray
argument_list|(
name|nonPrimitivesNames
argument_list|)
expr_stmt|;
name|Arrays
operator|.
name|sort
argument_list|(
name|nonPrimitivesNames
argument_list|)
expr_stmt|;
name|String
index|[]
name|primitivesNames
init|=
block|{
literal|"boolean"
block|,
literal|"byte"
block|,
literal|"char"
block|,
literal|"double"
block|,
literal|"float"
block|,
literal|"int"
block|,
literal|"long"
block|,
literal|"short"
block|}
decl_stmt|;
name|String
index|[]
name|finalList
init|=
operator|new
name|String
index|[
name|primitivesNames
operator|.
name|length
operator|+
name|nonPrimitivesNames
operator|.
name|length
operator|+
literal|1
index|]
decl_stmt|;
name|finalList
index|[
literal|0
index|]
operator|=
literal|""
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|primitivesNames
argument_list|,
literal|0
argument_list|,
name|finalList
argument_list|,
literal|1
argument_list|,
name|primitivesNames
operator|.
name|length
argument_list|)
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|nonPrimitivesNames
argument_list|,
literal|0
argument_list|,
name|finalList
argument_list|,
name|primitivesNames
operator|.
name|length
operator|+
literal|1
argument_list|,
name|nonPrimitivesNames
operator|.
name|length
argument_list|)
expr_stmt|;
return|return
name|finalList
return|;
block|}
specifier|public
specifier|static
name|DataNodeDescriptor
name|getNodeLinkedToMap
parameter_list|(
name|DataChannelDescriptor
name|domain
parameter_list|,
name|DataMap
name|map
parameter_list|)
block|{
name|Collection
argument_list|<
name|DataNodeDescriptor
argument_list|>
name|nodes
init|=
name|domain
operator|.
name|getNodeDescriptors
argument_list|()
decl_stmt|;
comment|// go via an iterator in an indexed loop, since
comment|// we already obtained the size
comment|// (and index is required to initialize array)
for|for
control|(
name|DataNodeDescriptor
name|node
range|:
name|nodes
control|)
if|if
condition|(
name|node
operator|.
name|getDataMapNames
argument_list|()
operator|.
name|contains
argument_list|(
name|map
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
return|return
name|node
return|;
return|return
literal|null
return|;
block|}
comment|/**      * Updates MultipleObjectActions' state, depending on number of selected objects      * (attributes, rel etc.)      */
specifier|public
specifier|static
name|void
name|updateActions
parameter_list|(
name|int
name|numSelected
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|Action
argument_list|>
modifier|...
name|actions
parameter_list|)
block|{
name|ActionManager
name|actionManager
init|=
name|Application
operator|.
name|getInstance
argument_list|()
operator|.
name|getActionManager
argument_list|()
decl_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
extends|extends
name|Action
argument_list|>
name|actionType
range|:
name|actions
control|)
block|{
name|Action
name|action
init|=
name|actionManager
operator|.
name|getAction
argument_list|(
name|actionType
argument_list|)
decl_stmt|;
if|if
condition|(
name|action
operator|instanceof
name|MultipleObjectsAction
condition|)
block|{
name|MultipleObjectsAction
name|multiObjectAction
init|=
operator|(
name|MultipleObjectsAction
operator|)
name|action
decl_stmt|;
name|multiObjectAction
operator|.
name|setEnabled
argument_list|(
name|numSelected
operator|>
literal|0
argument_list|)
expr_stmt|;
operator|(
operator|(
name|CayenneAction
operator|)
name|multiObjectAction
operator|)
operator|.
name|setName
argument_list|(
name|multiObjectAction
operator|.
name|getActionName
argument_list|(
name|numSelected
operator|>
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Retrieves strings from .properties file      */
specifier|public
specifier|static
name|String
name|getProperty
parameter_list|(
name|String
name|key
parameter_list|)
block|{
name|ResourceBundle
name|properties
init|=
name|ResourceBundle
operator|.
name|getBundle
argument_list|(
name|Application
operator|.
name|DEFAULT_MESSAGE_BUNDLE
argument_list|)
decl_stmt|;
return|return
name|properties
operator|==
literal|null
condition|?
literal|""
else|:
name|properties
operator|.
name|getString
argument_list|(
name|key
argument_list|)
return|;
block|}
comment|/**      * Center a window on a parent window      */
specifier|public
specifier|static
name|void
name|centerWindow
parameter_list|(
name|Window
name|parent
parameter_list|,
name|Window
name|child
parameter_list|)
block|{
name|Dimension
name|parentSize
init|=
name|parent
operator|.
name|getSize
argument_list|()
decl_stmt|;
name|Dimension
name|childSize
init|=
name|child
operator|.
name|getSize
argument_list|()
decl_stmt|;
name|Point
name|parentLocation
init|=
operator|new
name|Point
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|parent
operator|.
name|isShowing
argument_list|()
condition|)
block|{
name|parentLocation
operator|=
name|parent
operator|.
name|getLocationOnScreen
argument_list|()
expr_stmt|;
block|}
name|int
name|x
init|=
name|parentLocation
operator|.
name|x
operator|+
name|parentSize
operator|.
name|width
operator|/
literal|2
operator|-
name|childSize
operator|.
name|width
operator|/
literal|2
decl_stmt|;
name|int
name|y
init|=
name|parentLocation
operator|.
name|y
operator|+
name|parentSize
operator|.
name|height
operator|/
literal|2
operator|-
name|childSize
operator|.
name|height
operator|/
literal|2
decl_stmt|;
name|child
operator|.
name|setLocation
argument_list|(
name|x
argument_list|,
name|y
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

