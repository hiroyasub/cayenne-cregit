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
name|modeler
operator|.
name|pref
operator|.
name|DataMapDefaults
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
name|Map
operator|.
name|Entry
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

begin_class
specifier|public
class|class
name|CustomPreferencesUpdater
block|{
enum|enum
name|Property
block|{
name|SUBCLASS_TEMPLATE
block|,
name|SUPERCLASS_TEMPLATE
block|,
name|OVERWRITE
block|,
name|PAIRS
block|,
name|USE_PACKAGE_PATH
block|,
name|MODE
block|,
name|OUTPUT_PATTERN
block|}
specifier|private
specifier|static
specifier|final
name|String
name|OVERWRITE
init|=
literal|"overwrite"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PAIRS
init|=
literal|"pairs"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|USE_PACKAGE_PATH
init|=
literal|"usePackagePath"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|MODE
init|=
literal|"mode"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|OUTPUT_PATTERN
init|=
literal|"outputPattern"
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|DataMap
argument_list|,
name|DataMapDefaults
argument_list|>
name|mapPreferences
decl_stmt|;
specifier|public
name|CustomPreferencesUpdater
parameter_list|(
name|Map
argument_list|<
name|DataMap
argument_list|,
name|DataMapDefaults
argument_list|>
name|mapPreferences
parameter_list|)
block|{
name|this
operator|.
name|mapPreferences
operator|=
name|mapPreferences
expr_stmt|;
block|}
specifier|public
name|String
name|getMode
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|getProperty
argument_list|(
name|Property
operator|.
name|MODE
argument_list|)
return|;
block|}
specifier|public
name|void
name|setMode
parameter_list|(
name|String
name|mode
parameter_list|)
block|{
name|updatePreferences
argument_list|(
name|Property
operator|.
name|MODE
argument_list|,
name|mode
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getSubclassTemplate
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|getProperty
argument_list|(
name|Property
operator|.
name|SUBCLASS_TEMPLATE
argument_list|)
return|;
block|}
specifier|public
name|void
name|setSubclassTemplate
parameter_list|(
name|String
name|subclassTemplate
parameter_list|)
block|{
name|updatePreferences
argument_list|(
name|Property
operator|.
name|SUBCLASS_TEMPLATE
argument_list|,
name|subclassTemplate
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getSuperclassTemplate
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|getProperty
argument_list|(
name|Property
operator|.
name|SUPERCLASS_TEMPLATE
argument_list|)
return|;
block|}
specifier|public
name|void
name|setSuperclassTemplate
parameter_list|(
name|String
name|superclassTemplate
parameter_list|)
block|{
name|updatePreferences
argument_list|(
name|Property
operator|.
name|SUPERCLASS_TEMPLATE
argument_list|,
name|superclassTemplate
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Boolean
name|getOverwrite
parameter_list|()
block|{
return|return
operator|(
name|Boolean
operator|)
name|getProperty
argument_list|(
name|Property
operator|.
name|OVERWRITE
argument_list|)
return|;
block|}
specifier|public
name|void
name|setOverwrite
parameter_list|(
name|Boolean
name|overwrite
parameter_list|)
block|{
name|updatePreferences
argument_list|(
name|Property
operator|.
name|OVERWRITE
argument_list|,
name|overwrite
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Boolean
name|getPairs
parameter_list|()
block|{
return|return
operator|(
name|Boolean
operator|)
name|getProperty
argument_list|(
name|Property
operator|.
name|PAIRS
argument_list|)
return|;
block|}
specifier|public
name|void
name|setPairs
parameter_list|(
name|Boolean
name|pairs
parameter_list|)
block|{
name|updatePreferences
argument_list|(
name|Property
operator|.
name|PAIRS
argument_list|,
name|pairs
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Boolean
name|getUsePackagePath
parameter_list|()
block|{
return|return
operator|(
name|Boolean
operator|)
name|getProperty
argument_list|(
name|Property
operator|.
name|USE_PACKAGE_PATH
argument_list|)
return|;
block|}
specifier|public
name|void
name|setUsePackagePath
parameter_list|(
name|Boolean
name|usePackagePath
parameter_list|)
block|{
name|updatePreferences
argument_list|(
name|Property
operator|.
name|USE_PACKAGE_PATH
argument_list|,
name|usePackagePath
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getOutputPattern
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|getProperty
argument_list|(
name|Property
operator|.
name|OUTPUT_PATTERN
argument_list|)
return|;
block|}
specifier|public
name|void
name|setOutputPattern
parameter_list|(
name|String
name|outputPattern
parameter_list|)
block|{
name|updatePreferences
argument_list|(
name|Property
operator|.
name|OUTPUT_PATTERN
argument_list|,
name|outputPattern
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Object
name|getProperty
parameter_list|(
name|Property
name|property
parameter_list|)
block|{
name|Object
name|obj
init|=
literal|null
decl_stmt|;
name|Set
argument_list|<
name|Entry
argument_list|<
name|DataMap
argument_list|,
name|DataMapDefaults
argument_list|>
argument_list|>
name|entities
init|=
name|mapPreferences
operator|.
name|entrySet
argument_list|()
decl_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|DataMap
argument_list|,
name|DataMapDefaults
argument_list|>
name|entry
range|:
name|entities
control|)
block|{
switch|switch
condition|(
name|property
condition|)
block|{
case|case
name|MODE
case|:
name|obj
operator|=
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|getProperty
argument_list|(
name|MODE
argument_list|)
expr_stmt|;
break|break;
case|case
name|OUTPUT_PATTERN
case|:
name|obj
operator|=
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|getProperty
argument_list|(
name|OUTPUT_PATTERN
argument_list|)
expr_stmt|;
break|break;
case|case
name|SUBCLASS_TEMPLATE
case|:
name|obj
operator|=
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|getSubclassTemplate
argument_list|()
expr_stmt|;
break|break;
case|case
name|SUPERCLASS_TEMPLATE
case|:
name|obj
operator|=
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|getSuperclassTemplate
argument_list|()
expr_stmt|;
break|break;
case|case
name|OVERWRITE
case|:
name|obj
operator|=
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|getBooleanProperty
argument_list|(
name|OVERWRITE
argument_list|)
expr_stmt|;
break|break;
case|case
name|PAIRS
case|:
name|obj
operator|=
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|getBooleanProperty
argument_list|(
name|PAIRS
argument_list|)
expr_stmt|;
break|break;
case|case
name|USE_PACKAGE_PATH
case|:
name|obj
operator|=
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|getBooleanProperty
argument_list|(
name|USE_PACKAGE_PATH
argument_list|)
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Bad type property: "
operator|+
name|property
argument_list|)
throw|;
block|}
block|}
return|return
name|obj
return|;
block|}
specifier|private
name|void
name|updatePreferences
parameter_list|(
name|Property
name|property
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|Set
argument_list|<
name|Entry
argument_list|<
name|DataMap
argument_list|,
name|DataMapDefaults
argument_list|>
argument_list|>
name|entities
init|=
name|mapPreferences
operator|.
name|entrySet
argument_list|()
decl_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|DataMap
argument_list|,
name|DataMapDefaults
argument_list|>
name|entry
range|:
name|entities
control|)
block|{
switch|switch
condition|(
name|property
condition|)
block|{
case|case
name|MODE
case|:
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|setProperty
argument_list|(
name|MODE
argument_list|,
operator|(
name|String
operator|)
name|value
argument_list|)
expr_stmt|;
break|break;
case|case
name|OUTPUT_PATTERN
case|:
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|setProperty
argument_list|(
name|OUTPUT_PATTERN
argument_list|,
operator|(
name|String
operator|)
name|value
argument_list|)
expr_stmt|;
break|break;
case|case
name|SUBCLASS_TEMPLATE
case|:
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|setSubclassTemplate
argument_list|(
operator|(
name|String
operator|)
name|value
argument_list|)
expr_stmt|;
break|break;
case|case
name|SUPERCLASS_TEMPLATE
case|:
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|setSuperclassTemplate
argument_list|(
operator|(
name|String
operator|)
name|value
argument_list|)
expr_stmt|;
break|break;
case|case
name|OVERWRITE
case|:
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|setBooleanProperty
argument_list|(
name|OVERWRITE
argument_list|,
operator|(
name|Boolean
operator|)
name|value
argument_list|)
expr_stmt|;
break|break;
case|case
name|PAIRS
case|:
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|setBooleanProperty
argument_list|(
name|PAIRS
argument_list|,
operator|(
name|Boolean
operator|)
name|value
argument_list|)
expr_stmt|;
break|break;
case|case
name|USE_PACKAGE_PATH
case|:
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|setBooleanProperty
argument_list|(
name|USE_PACKAGE_PATH
argument_list|,
operator|(
name|Boolean
operator|)
name|value
argument_list|)
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Bad type property: "
operator|+
name|property
argument_list|)
throw|;
block|}
block|}
block|}
block|}
end_class

end_unit

