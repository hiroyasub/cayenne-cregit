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
name|modeler
operator|.
name|pref
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
name|pref
operator|.
name|RenamedPreferences
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
name|java
operator|.
name|util
operator|.
name|prefs
operator|.
name|Preferences
import|;
end_import

begin_class
specifier|public
class|class
name|DataMapDefaults
extends|extends
name|RenamedPreferences
block|{
specifier|private
name|boolean
name|generatePairs
decl_stmt|;
specifier|private
name|String
name|outputPath
decl_stmt|;
specifier|private
name|String
name|subclassTemplate
decl_stmt|;
specifier|private
name|String
name|superclassPackage
decl_stmt|;
specifier|private
name|String
name|superclassTemplate
decl_stmt|;
specifier|private
name|boolean
name|initGeneratePairs
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|GENERATE_PAIRS_PROPERTY
init|=
literal|"generatePairs"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|OUTPUT_PATH_PROPERTY
init|=
literal|"outputPath"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SUBCLASS_TEMPLATE_PROPERTY
init|=
literal|"subclassTemplate"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SUPERCLASS_PACKAGE_PROPERTY
init|=
literal|"superclassPackage"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SUPERCLASS_TEMPLATE_PROPERTY
init|=
literal|"superclassTemplate"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_SUPERCLASS_PACKAGE_SUFFIX
init|=
literal|"auto"
decl_stmt|;
specifier|public
name|DataMapDefaults
parameter_list|(
name|Preferences
name|pref
parameter_list|)
block|{
name|super
argument_list|(
name|pref
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns a superclass package tailored for a given DataMap.      */
specifier|public
name|void
name|updateSuperclassPackage
parameter_list|(
name|DataMap
name|dataMap
parameter_list|)
block|{
name|String
name|storedPackage
init|=
name|getSuperclassPackage
argument_list|()
decl_stmt|;
if|if
condition|(
name|Util
operator|.
name|isEmptyString
argument_list|(
name|storedPackage
argument_list|)
operator|||
name|DEFAULT_SUPERCLASS_PACKAGE_SUFFIX
operator|.
name|equals
argument_list|(
name|storedPackage
argument_list|)
condition|)
block|{
name|String
name|mapPackage
init|=
name|dataMap
operator|.
name|getDefaultPackage
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|Util
operator|.
name|isEmptyString
argument_list|(
name|mapPackage
argument_list|)
condition|)
block|{
if|if
condition|(
name|mapPackage
operator|.
name|endsWith
argument_list|(
literal|"."
argument_list|)
condition|)
block|{
name|mapPackage
operator|=
name|mapPackage
operator|.
name|substring
argument_list|(
name|mapPackage
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|Util
operator|.
name|isEmptyString
argument_list|(
name|mapPackage
argument_list|)
condition|)
block|{
name|String
name|newPackage
init|=
name|mapPackage
operator|+
literal|"."
operator|+
name|DEFAULT_SUPERCLASS_PACKAGE_SUFFIX
decl_stmt|;
if|if
condition|(
operator|!
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|newPackage
argument_list|,
name|storedPackage
argument_list|)
condition|)
block|{
name|setSuperclassPackage
argument_list|(
name|newPackage
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
if|if
condition|(
name|DEFAULT_SUPERCLASS_PACKAGE_SUFFIX
operator|.
name|equals
argument_list|(
name|getSuperclassPackage
argument_list|()
argument_list|)
condition|)
block|{
name|setSuperclassPackage
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * An initialization callback.      */
specifier|public
name|void
name|prePersist
parameter_list|()
block|{
name|setGeneratePairs
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets superclass package, building it by "normalizing" and concatenating prefix and      * suffix.      */
specifier|public
name|void
name|setSuperclassPackage
parameter_list|(
name|String
name|prefix
parameter_list|,
name|String
name|suffix
parameter_list|)
block|{
if|if
condition|(
name|prefix
operator|==
literal|null
condition|)
block|{
name|prefix
operator|=
literal|""
expr_stmt|;
block|}
if|else if
condition|(
name|prefix
operator|.
name|endsWith
argument_list|(
literal|"."
argument_list|)
condition|)
block|{
name|prefix
operator|=
name|prefix
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|prefix
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|suffix
operator|==
literal|null
condition|)
block|{
name|suffix
operator|=
literal|""
expr_stmt|;
block|}
if|else if
condition|(
name|suffix
operator|.
name|startsWith
argument_list|(
literal|"."
argument_list|)
condition|)
block|{
name|suffix
operator|=
name|suffix
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
name|String
name|dot
init|=
operator|(
name|suffix
operator|.
name|length
argument_list|()
operator|>
literal|0
operator|&&
name|prefix
operator|.
name|length
argument_list|()
operator|>
literal|0
operator|)
condition|?
literal|"."
else|:
literal|""
decl_stmt|;
name|setSuperclassPackage
argument_list|(
name|prefix
operator|+
name|dot
operator|+
name|suffix
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|getGeneratePairs
parameter_list|()
block|{
if|if
condition|(
operator|!
name|initGeneratePairs
condition|)
block|{
name|generatePairs
operator|=
name|getCurrentPreference
argument_list|()
operator|.
name|getBoolean
argument_list|(
name|GENERATE_PAIRS_PROPERTY
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|initGeneratePairs
operator|=
literal|true
expr_stmt|;
block|}
return|return
name|generatePairs
return|;
block|}
specifier|public
name|void
name|setGeneratePairs
parameter_list|(
name|Boolean
name|bool
parameter_list|)
block|{
if|if
condition|(
name|getCurrentPreference
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|generatePairs
operator|=
name|bool
expr_stmt|;
name|getCurrentPreference
argument_list|()
operator|.
name|putBoolean
argument_list|(
name|GENERATE_PAIRS_PROPERTY
argument_list|,
name|bool
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|String
name|getOutputPath
parameter_list|()
block|{
if|if
condition|(
name|outputPath
operator|==
literal|null
condition|)
block|{
name|outputPath
operator|=
name|getCurrentPreference
argument_list|()
operator|.
name|get
argument_list|(
name|OUTPUT_PATH_PROPERTY
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
return|return
name|outputPath
return|;
block|}
specifier|public
name|void
name|setOutputPath
parameter_list|(
name|String
name|outputPath
parameter_list|)
block|{
if|if
condition|(
name|getCurrentPreference
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|outputPath
operator|=
name|outputPath
expr_stmt|;
if|if
condition|(
name|outputPath
operator|==
literal|null
condition|)
block|{
name|outputPath
operator|=
literal|""
expr_stmt|;
block|}
name|getCurrentPreference
argument_list|()
operator|.
name|put
argument_list|(
name|OUTPUT_PATH_PROPERTY
argument_list|,
name|outputPath
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|String
name|getSubclassTemplate
parameter_list|()
block|{
if|if
condition|(
name|subclassTemplate
operator|==
literal|null
condition|)
block|{
name|subclassTemplate
operator|=
name|getCurrentPreference
argument_list|()
operator|.
name|get
argument_list|(
name|SUBCLASS_TEMPLATE_PROPERTY
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
return|return
name|subclassTemplate
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
if|if
condition|(
name|getCurrentPreference
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|subclassTemplate
operator|=
name|subclassTemplate
expr_stmt|;
if|if
condition|(
name|subclassTemplate
operator|==
literal|null
condition|)
block|{
name|subclassTemplate
operator|=
literal|""
expr_stmt|;
block|}
name|getCurrentPreference
argument_list|()
operator|.
name|put
argument_list|(
name|SUBCLASS_TEMPLATE_PROPERTY
argument_list|,
name|subclassTemplate
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|String
name|getSuperclassPackage
parameter_list|()
block|{
if|if
condition|(
name|superclassPackage
operator|==
literal|null
condition|)
block|{
name|superclassPackage
operator|=
name|getCurrentPreference
argument_list|()
operator|.
name|get
argument_list|(
name|SUPERCLASS_PACKAGE_PROPERTY
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
return|return
name|superclassPackage
return|;
block|}
specifier|public
name|void
name|setSuperclassPackage
parameter_list|(
name|String
name|superclassPackage
parameter_list|)
block|{
if|if
condition|(
name|getCurrentPreference
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|superclassPackage
operator|=
name|superclassPackage
expr_stmt|;
if|if
condition|(
name|superclassPackage
operator|==
literal|null
condition|)
block|{
name|superclassPackage
operator|=
literal|""
expr_stmt|;
block|}
name|getCurrentPreference
argument_list|()
operator|.
name|put
argument_list|(
name|SUPERCLASS_PACKAGE_PROPERTY
argument_list|,
name|superclassPackage
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|String
name|getSuperclassTemplate
parameter_list|()
block|{
if|if
condition|(
name|superclassTemplate
operator|==
literal|null
condition|)
block|{
name|superclassTemplate
operator|=
name|getCurrentPreference
argument_list|()
operator|.
name|get
argument_list|(
name|SUPERCLASS_TEMPLATE_PROPERTY
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
return|return
name|superclassTemplate
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
if|if
condition|(
name|getCurrentPreference
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|superclassTemplate
operator|=
name|superclassTemplate
expr_stmt|;
if|if
condition|(
name|superclassTemplate
operator|==
literal|null
condition|)
block|{
name|superclassTemplate
operator|=
literal|""
expr_stmt|;
block|}
name|getCurrentPreference
argument_list|()
operator|.
name|put
argument_list|(
name|SUPERCLASS_TEMPLATE_PROPERTY
argument_list|,
name|superclassTemplate
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|String
name|getProperty
parameter_list|(
name|String
name|property
parameter_list|)
block|{
if|if
condition|(
name|property
operator|!=
literal|null
operator|&&
name|getCurrentPreference
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|getCurrentPreference
argument_list|()
operator|.
name|get
argument_list|(
name|property
argument_list|,
literal|null
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|setProperty
parameter_list|(
name|String
name|property
parameter_list|,
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
name|getCurrentPreference
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
name|value
operator|=
literal|""
expr_stmt|;
block|}
name|getCurrentPreference
argument_list|()
operator|.
name|put
argument_list|(
name|property
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setBooleanProperty
parameter_list|(
name|String
name|property
parameter_list|,
name|boolean
name|value
parameter_list|)
block|{
if|if
condition|(
name|getCurrentPreference
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|getCurrentPreference
argument_list|()
operator|.
name|putBoolean
argument_list|(
name|property
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|boolean
name|getBooleanProperty
parameter_list|(
name|String
name|property
parameter_list|)
block|{
if|if
condition|(
name|property
operator|!=
literal|null
operator|&&
name|getCurrentPreference
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|getCurrentPreference
argument_list|()
operator|.
name|getBoolean
argument_list|(
name|property
argument_list|,
literal|false
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

