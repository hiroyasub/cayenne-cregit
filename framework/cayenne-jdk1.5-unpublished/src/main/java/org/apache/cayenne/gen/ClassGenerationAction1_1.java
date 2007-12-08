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
name|gen
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Writer
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
name|CayenneDataObject
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
name|CayenneRuntimeException
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
comment|/**  * @since 3.0  * @author Andrus Adamchik  * @deprecated since 3.0  */
end_comment

begin_class
specifier|public
class|class
name|ClassGenerationAction1_1
extends|extends
name|ClassGenerationAction
block|{
specifier|public
specifier|static
specifier|final
name|String
name|SINGLE_CLASS_TEMPLATE
init|=
literal|"dotemplates/singleclass.vm"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SUBCLASS_TEMPLATE
init|=
literal|"dotemplates/subclass.vm"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SUPERCLASS_TEMPLATE
init|=
literal|"dotemplates/superclass.vm"
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|String
name|defaultSingleClassTemplate
parameter_list|()
block|{
return|return
name|ClassGenerationAction1_1
operator|.
name|SINGLE_CLASS_TEMPLATE
return|;
block|}
annotation|@
name|Override
specifier|protected
name|String
name|defaultSubclassTemplate
parameter_list|()
block|{
return|return
name|ClassGenerationAction1_1
operator|.
name|SUBCLASS_TEMPLATE
return|;
block|}
annotation|@
name|Override
specifier|protected
name|String
name|defaultSuperclassTemplate
parameter_list|()
block|{
return|return
name|ClassGenerationAction1_1
operator|.
name|SUPERCLASS_TEMPLATE
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|generateClassPairs
parameter_list|(
name|String
name|classTemplate
parameter_list|,
name|String
name|superTemplate
parameter_list|,
name|String
name|superPrefix
parameter_list|)
throws|throws
name|Exception
block|{
name|TemplateProcessor1_1
name|mainGenerator
init|=
operator|new
name|TemplateProcessor1_1
argument_list|(
name|classTemplate
argument_list|)
decl_stmt|;
name|TemplateProcessor1_1
name|superGenerator
init|=
operator|new
name|TemplateProcessor1_1
argument_list|(
name|superTemplate
argument_list|)
decl_stmt|;
name|ClassGenerationInfo
name|mainGen
init|=
name|mainGenerator
operator|.
name|getClassGenerationInfo
argument_list|()
decl_stmt|;
name|ClassGenerationInfo
name|superGen
init|=
name|superGenerator
operator|.
name|getClassGenerationInfo
argument_list|()
decl_stmt|;
comment|// prefix is needed for both generators
name|mainGen
operator|.
name|setSuperPrefix
argument_list|(
name|superPrefix
argument_list|)
expr_stmt|;
name|superGen
operator|.
name|setSuperPrefix
argument_list|(
name|superPrefix
argument_list|)
expr_stmt|;
for|for
control|(
name|ObjEntity
name|entity
range|:
name|entitiesForCurrentMode
argument_list|()
control|)
block|{
comment|// 1. do the superclass
name|initClassGenerator
argument_list|(
name|superGen
argument_list|,
name|entity
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|Writer
name|superOut
init|=
name|openWriter
argument_list|(
name|superGen
operator|.
name|getPackageName
argument_list|()
argument_list|,
name|superPrefix
operator|+
name|superGen
operator|.
name|getClassName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|superOut
operator|!=
literal|null
condition|)
block|{
name|superGenerator
operator|.
name|generateClass
argument_list|(
name|superOut
argument_list|,
name|dataMap
argument_list|,
name|entity
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|superOut
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
comment|// 2. do the main class
name|initClassGenerator
argument_list|(
name|mainGen
argument_list|,
name|entity
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|Writer
name|mainOut
init|=
name|openWriter
argument_list|(
name|mainGen
operator|.
name|getPackageName
argument_list|()
argument_list|,
name|mainGen
operator|.
name|getClassName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|mainOut
operator|!=
literal|null
condition|)
block|{
name|mainGenerator
operator|.
name|generateClass
argument_list|(
name|mainOut
argument_list|,
name|dataMap
argument_list|,
name|entity
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|mainOut
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|generateSingleClasses
parameter_list|(
name|String
name|classTemplate
parameter_list|,
name|String
name|superPrefix
parameter_list|)
throws|throws
name|Exception
block|{
name|TemplateProcessor1_1
name|generator
init|=
operator|new
name|TemplateProcessor1_1
argument_list|(
name|classTemplate
argument_list|)
decl_stmt|;
for|for
control|(
name|ObjEntity
name|entity
range|:
name|entitiesForCurrentMode
argument_list|()
control|)
block|{
name|initClassGenerator
argument_list|(
name|generator
operator|.
name|getClassGenerationInfo
argument_list|()
argument_list|,
name|entity
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|Writer
name|out
init|=
name|openWriter
argument_list|(
name|generator
operator|.
name|getClassGenerationInfo
argument_list|()
operator|.
name|getPackageName
argument_list|()
argument_list|,
name|generator
operator|.
name|getClassGenerationInfo
argument_list|()
operator|.
name|getClassName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|out
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
name|generator
operator|.
name|generateClass
argument_list|(
name|out
argument_list|,
name|dataMap
argument_list|,
name|entity
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Initializes ClassGenerationInfo with class name and package of a generated class.      */
specifier|private
name|void
name|initClassGenerator
parameter_list|(
name|ClassGenerationInfo
name|generatorInfo
parameter_list|,
name|ObjEntity
name|entity
parameter_list|,
name|boolean
name|superclass
parameter_list|)
block|{
comment|// figure out generator properties
name|String
name|fullClassName
init|=
name|entity
operator|.
name|getClassName
argument_list|()
decl_stmt|;
name|int
name|i
init|=
name|fullClassName
operator|.
name|lastIndexOf
argument_list|(
literal|"."
argument_list|)
decl_stmt|;
name|String
name|pkg
init|=
literal|null
decl_stmt|;
name|String
name|spkg
init|=
literal|null
decl_stmt|;
name|String
name|cname
init|=
literal|null
decl_stmt|;
comment|// dot in first or last position is invalid
if|if
condition|(
name|i
operator|==
literal|0
operator|||
name|i
operator|+
literal|1
operator|==
name|fullClassName
operator|.
name|length
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Invalid class mapping: "
operator|+
name|fullClassName
argument_list|)
throw|;
block|}
if|else if
condition|(
name|i
operator|<
literal|0
condition|)
block|{
name|pkg
operator|=
operator|(
name|superclass
operator|)
condition|?
name|superPkg
else|:
literal|null
expr_stmt|;
name|spkg
operator|=
operator|(
name|superclass
operator|)
condition|?
literal|null
else|:
name|superPkg
expr_stmt|;
name|cname
operator|=
name|fullClassName
expr_stmt|;
block|}
else|else
block|{
name|cname
operator|=
name|fullClassName
operator|.
name|substring
argument_list|(
name|i
operator|+
literal|1
argument_list|)
expr_stmt|;
name|pkg
operator|=
operator|(
name|superclass
operator|&&
name|superPkg
operator|!=
literal|null
operator|)
condition|?
name|superPkg
else|:
name|fullClassName
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|i
argument_list|)
expr_stmt|;
name|spkg
operator|=
operator|(
operator|!
name|superclass
operator|&&
name|superPkg
operator|!=
literal|null
operator|&&
operator|!
name|pkg
operator|.
name|equals
argument_list|(
name|superPkg
argument_list|)
operator|)
condition|?
name|superPkg
else|:
literal|null
expr_stmt|;
block|}
name|generatorInfo
operator|.
name|setPackageName
argument_list|(
name|pkg
argument_list|)
expr_stmt|;
name|generatorInfo
operator|.
name|setClassName
argument_list|(
name|cname
argument_list|)
expr_stmt|;
if|if
condition|(
name|entity
operator|.
name|getSuperClassName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|generatorInfo
operator|.
name|setSuperClassName
argument_list|(
name|entity
operator|.
name|getSuperClassName
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|generatorInfo
operator|.
name|setSuperClassName
argument_list|(
name|CayenneDataObject
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|generatorInfo
operator|.
name|setSuperPackageName
argument_list|(
name|spkg
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

