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
name|pref
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
name|java
operator|.
name|awt
operator|.
name|event
operator|.
name|ComponentAdapter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|event
operator|.
name|ComponentEvent
import|;
end_import

begin_import
import|import
name|java
operator|.
name|beans
operator|.
name|PropertyChangeEvent
import|;
end_import

begin_import
import|import
name|java
operator|.
name|beans
operator|.
name|PropertyChangeListener
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JDialog
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JFrame
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
name|pref
operator|.
name|Domain
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
name|pref
operator|.
name|PreferenceException
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

begin_class
specifier|public
class|class
name|ComponentGeometry
extends|extends
name|_ComponentGeometry
block|{
specifier|public
specifier|static
specifier|final
name|String
name|GEOMETRY_PREF_KEY
init|=
literal|"geometry"
decl_stmt|;
specifier|public
specifier|static
name|ComponentGeometry
name|getPreference
parameter_list|(
name|Domain
name|domain
parameter_list|)
block|{
return|return
operator|(
name|ComponentGeometry
operator|)
name|domain
operator|.
name|getDetail
argument_list|(
name|ComponentGeometry
operator|.
name|GEOMETRY_PREF_KEY
argument_list|,
name|ComponentGeometry
operator|.
name|class
argument_list|,
literal|true
argument_list|)
return|;
block|}
comment|/**      * Binds this preference object to synchronize its state with a given component,      * allowing to specify an initial offset compared to the stored position.      */
specifier|public
name|void
name|bind
parameter_list|(
specifier|final
name|JFrame
name|frame
parameter_list|,
name|int
name|initialWidth
parameter_list|,
name|int
name|initialHeight
parameter_list|,
name|int
name|maxOffset
parameter_list|)
block|{
name|updateSize
argument_list|(
name|frame
argument_list|,
name|initialWidth
argument_list|,
name|initialHeight
argument_list|)
expr_stmt|;
name|updateLocation
argument_list|(
name|frame
argument_list|,
name|maxOffset
argument_list|)
expr_stmt|;
name|frame
operator|.
name|addComponentListener
argument_list|(
operator|new
name|ComponentAdapter
argument_list|()
block|{
specifier|public
name|void
name|componentResized
parameter_list|(
name|ComponentEvent
name|e
parameter_list|)
block|{
name|setWidth
argument_list|(
operator|new
name|Integer
argument_list|(
name|frame
operator|.
name|getWidth
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|setHeight
argument_list|(
operator|new
name|Integer
argument_list|(
name|frame
operator|.
name|getHeight
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|componentMoved
parameter_list|(
name|ComponentEvent
name|e
parameter_list|)
block|{
name|setX
argument_list|(
operator|new
name|Integer
argument_list|(
name|frame
operator|.
name|getX
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|setY
argument_list|(
operator|new
name|Integer
argument_list|(
name|frame
operator|.
name|getY
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
comment|/**      * Binds this preference object to synchronize its state with a given component,      * allowing to specify an initial offset compared to the stored position.      */
specifier|public
name|void
name|bind
parameter_list|(
specifier|final
name|JDialog
name|dialog
parameter_list|,
name|int
name|initialWidth
parameter_list|,
name|int
name|initialHeight
parameter_list|)
block|{
name|updateSize
argument_list|(
name|dialog
argument_list|,
name|initialWidth
argument_list|,
name|initialHeight
argument_list|)
expr_stmt|;
name|dialog
operator|.
name|addComponentListener
argument_list|(
operator|new
name|ComponentAdapter
argument_list|()
block|{
specifier|public
name|void
name|componentResized
parameter_list|(
name|ComponentEvent
name|e
parameter_list|)
block|{
name|setWidth
argument_list|(
operator|new
name|Integer
argument_list|(
name|dialog
operator|.
name|getWidth
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|setHeight
argument_list|(
operator|new
name|Integer
argument_list|(
name|dialog
operator|.
name|getHeight
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
comment|/**      * Binds this preference object to synchronize its state with a given component      * property.      */
specifier|public
name|void
name|bindIntProperty
parameter_list|(
specifier|final
name|Component
name|component
parameter_list|,
specifier|final
name|String
name|property
parameter_list|,
name|int
name|defaultValue
parameter_list|)
block|{
name|updateIntProperty
argument_list|(
name|component
argument_list|,
name|property
argument_list|,
name|defaultValue
argument_list|)
expr_stmt|;
name|component
operator|.
name|addPropertyChangeListener
argument_list|(
name|property
argument_list|,
operator|new
name|PropertyChangeListener
argument_list|()
block|{
specifier|public
name|void
name|propertyChange
parameter_list|(
name|PropertyChangeEvent
name|e
parameter_list|)
block|{
name|Object
name|value
init|=
name|e
operator|.
name|getNewValue
argument_list|()
decl_stmt|;
name|setProperty
argument_list|(
name|property
argument_list|,
name|value
operator|!=
literal|null
condition|?
name|value
operator|.
name|toString
argument_list|()
else|:
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
name|void
name|updateIntProperty
parameter_list|(
name|Component
name|c
parameter_list|,
name|String
name|property
parameter_list|,
name|int
name|defaultValue
parameter_list|)
block|{
name|int
name|i
init|=
name|getIntProperty
argument_list|(
name|property
argument_list|,
name|defaultValue
argument_list|)
decl_stmt|;
try|try
block|{
name|PropertyUtils
operator|.
name|setProperty
argument_list|(
name|c
argument_list|,
name|property
argument_list|,
operator|new
name|Integer
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|th
parameter_list|)
block|{
throw|throw
operator|new
name|PreferenceException
argument_list|(
literal|"Error setting property: "
operator|+
name|property
argument_list|,
name|th
argument_list|)
throw|;
block|}
block|}
name|void
name|updateSize
parameter_list|(
name|Component
name|c
parameter_list|,
name|int
name|initialWidth
parameter_list|,
name|int
name|initialHeight
parameter_list|)
block|{
name|int
name|w
init|=
name|getIntWidth
argument_list|(
name|initialWidth
argument_list|)
decl_stmt|;
name|int
name|h
init|=
name|getIntHeight
argument_list|(
name|initialHeight
argument_list|)
decl_stmt|;
if|if
condition|(
name|w
operator|>
literal|0
operator|&&
name|h
operator|>
literal|0
condition|)
block|{
name|c
operator|.
name|setSize
argument_list|(
name|w
argument_list|,
name|h
argument_list|)
expr_stmt|;
block|}
block|}
name|void
name|updateLocation
parameter_list|(
name|Component
name|c
parameter_list|,
name|int
name|maxOffset
parameter_list|)
block|{
if|if
condition|(
name|maxOffset
operator|!=
literal|0
condition|)
block|{
name|int
name|xOffset
init|=
operator|(
name|int
operator|)
operator|(
name|Math
operator|.
name|random
argument_list|()
operator|*
name|maxOffset
operator|)
decl_stmt|;
name|int
name|yOffset
init|=
operator|(
name|int
operator|)
operator|(
name|Math
operator|.
name|random
argument_list|()
operator|*
name|maxOffset
operator|)
decl_stmt|;
name|changeX
argument_list|(
name|xOffset
argument_list|)
expr_stmt|;
name|changeY
argument_list|(
name|yOffset
argument_list|)
expr_stmt|;
block|}
name|int
name|x
init|=
name|getIntX
argument_list|(
operator|-
literal|1
argument_list|)
decl_stmt|;
name|int
name|y
init|=
name|getIntY
argument_list|(
operator|-
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|x
operator|>
literal|0
operator|&&
name|y
operator|>
literal|0
condition|)
block|{
name|c
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
specifier|public
name|void
name|changeX
parameter_list|(
name|int
name|xOffset
parameter_list|)
block|{
if|if
condition|(
name|xOffset
operator|!=
literal|0
condition|)
block|{
name|setX
argument_list|(
operator|new
name|Integer
argument_list|(
name|getIntX
argument_list|(
literal|0
argument_list|)
operator|+
name|xOffset
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|changeY
parameter_list|(
name|int
name|yOffset
parameter_list|)
block|{
if|if
condition|(
name|yOffset
operator|!=
literal|0
condition|)
block|{
name|setY
argument_list|(
operator|new
name|Integer
argument_list|(
name|getIntY
argument_list|(
literal|0
argument_list|)
operator|+
name|yOffset
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|int
name|getIntWidth
parameter_list|(
name|int
name|defaultValue
parameter_list|)
block|{
return|return
operator|(
name|getWidth
argument_list|()
operator|!=
literal|null
operator|)
condition|?
name|getWidth
argument_list|()
operator|.
name|intValue
argument_list|()
else|:
name|defaultValue
return|;
block|}
specifier|public
name|int
name|getIntHeight
parameter_list|(
name|int
name|defaultValue
parameter_list|)
block|{
return|return
operator|(
name|getHeight
argument_list|()
operator|!=
literal|null
operator|)
condition|?
name|getHeight
argument_list|()
operator|.
name|intValue
argument_list|()
else|:
name|defaultValue
return|;
block|}
specifier|public
name|int
name|getIntX
parameter_list|(
name|int
name|defaultValue
parameter_list|)
block|{
return|return
operator|(
name|getX
argument_list|()
operator|!=
literal|null
operator|)
condition|?
name|getX
argument_list|()
operator|.
name|intValue
argument_list|()
else|:
name|defaultValue
return|;
block|}
specifier|public
name|int
name|getIntY
parameter_list|(
name|int
name|defaultValue
parameter_list|)
block|{
return|return
operator|(
name|getY
argument_list|()
operator|!=
literal|null
operator|)
condition|?
name|getY
argument_list|()
operator|.
name|intValue
argument_list|()
else|:
name|defaultValue
return|;
block|}
block|}
end_class

end_unit

