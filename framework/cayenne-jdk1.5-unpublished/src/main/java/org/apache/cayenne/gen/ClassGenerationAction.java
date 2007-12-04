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
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStreamWriter
import|;
end_import

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
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|tools
operator|.
name|NamePatternMatcher
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|velocity
operator|.
name|VelocityContext
import|;
end_import

begin_comment
comment|/**  * @since 3.0  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|ClassGenerationAction
block|{
specifier|public
specifier|static
specifier|final
name|String
name|SINGLE_CLASS_TEMPLATE
init|=
literal|"dotemplates/v1_2/singleclass.vm"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SUBCLASS_TEMPLATE
init|=
literal|"dotemplates/v1_2/subclass.vm"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SUPERCLASS_TEMPLATE
init|=
literal|"dotemplates/v1_2/superclass.vm"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SUPERCLASS_PREFIX
init|=
literal|"_"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|WILDCARD
init|=
literal|"*"
decl_stmt|;
specifier|protected
name|Collection
argument_list|<
name|ObjEntity
argument_list|>
name|entities
decl_stmt|;
specifier|protected
name|String
name|superPkg
decl_stmt|;
specifier|protected
name|DataMap
name|dataMap
decl_stmt|;
specifier|protected
name|ClassGeneratorMode
name|mode
decl_stmt|;
specifier|protected
name|VelocityContext
name|context
decl_stmt|;
specifier|protected
name|Log
name|logger
decl_stmt|;
specifier|protected
name|File
name|destDir
decl_stmt|;
specifier|protected
name|boolean
name|overwrite
decl_stmt|;
specifier|protected
name|boolean
name|usePkgPath
decl_stmt|;
specifier|protected
name|boolean
name|makePairs
decl_stmt|;
specifier|protected
name|String
name|template
decl_stmt|;
specifier|protected
name|String
name|superTemplate
decl_stmt|;
specifier|protected
name|long
name|timestamp
decl_stmt|;
specifier|protected
name|String
name|outputPattern
decl_stmt|;
specifier|protected
name|String
name|encoding
decl_stmt|;
specifier|public
name|ClassGenerationAction
parameter_list|()
block|{
name|this
operator|.
name|mode
operator|=
name|ClassGeneratorMode
operator|.
name|entity
expr_stmt|;
name|this
operator|.
name|outputPattern
operator|=
literal|"*.java"
expr_stmt|;
name|this
operator|.
name|timestamp
operator|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
expr_stmt|;
name|this
operator|.
name|usePkgPath
operator|=
literal|true
expr_stmt|;
name|this
operator|.
name|makePairs
operator|=
literal|true
expr_stmt|;
block|}
specifier|public
name|String
name|defaultSingleClassTemplate
parameter_list|()
block|{
return|return
name|ClassGenerationAction
operator|.
name|SINGLE_CLASS_TEMPLATE
return|;
block|}
specifier|public
name|String
name|defaultSubclassTemplate
parameter_list|()
block|{
return|return
name|ClassGenerationAction
operator|.
name|SUBCLASS_TEMPLATE
return|;
block|}
specifier|public
name|String
name|defaultSuperclassTemplate
parameter_list|()
block|{
return|return
name|ClassGenerationAction
operator|.
name|SUPERCLASS_TEMPLATE
return|;
block|}
comment|/**      * Runs class generation. Produces a pair of Java classes for each ObjEntity in the      * map. This allows developers to use generated<b>subclass</b> for their custom      * code, while generated<b>superclass</b> will contain Cayenne code. Superclass will      * be generated in the same package, its class name will be derived from the class      * name by adding a<code>superPrefix</code>.      */
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
name|TemplateProcessor
name|mainGenSetup
init|=
operator|new
name|TemplateProcessor
argument_list|(
name|classTemplate
argument_list|,
name|context
argument_list|)
decl_stmt|;
name|TemplateProcessor
name|superGenSetup
init|=
operator|new
name|TemplateProcessor
argument_list|(
name|superTemplate
argument_list|,
name|context
argument_list|)
decl_stmt|;
name|StringUtils
name|stringUtils
init|=
name|StringUtils
operator|.
name|getInstance
argument_list|()
decl_stmt|;
for|for
control|(
name|ObjEntity
name|ent
range|:
name|entitiesForCurrentMode
argument_list|()
control|)
block|{
name|String
name|fqnSubClass
init|=
name|ent
operator|.
name|getClassName
argument_list|()
decl_stmt|;
name|String
name|fqnBaseClass
init|=
operator|(
name|ent
operator|.
name|getSuperClassName
argument_list|()
operator|!=
literal|null
operator|)
condition|?
name|ent
operator|.
name|getSuperClassName
argument_list|()
else|:
name|CayenneDataObject
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
name|String
name|subClassName
init|=
name|stringUtils
operator|.
name|stripPackageName
argument_list|(
name|fqnSubClass
argument_list|)
decl_stmt|;
name|String
name|subPackageName
init|=
name|stringUtils
operator|.
name|stripClass
argument_list|(
name|fqnSubClass
argument_list|)
decl_stmt|;
name|String
name|superClassName
init|=
name|superPrefix
operator|+
name|stringUtils
operator|.
name|stripPackageName
argument_list|(
name|fqnSubClass
argument_list|)
decl_stmt|;
name|String
name|superPackageName
init|=
name|this
operator|.
name|superPkg
decl_stmt|;
if|if
condition|(
name|superPackageName
operator|==
literal|null
condition|)
block|{
name|superPackageName
operator|=
name|subPackageName
expr_stmt|;
block|}
name|String
name|fqnSuperClass
init|=
name|superPackageName
operator|+
literal|"."
operator|+
name|superClassName
decl_stmt|;
name|Writer
name|superOut
init|=
name|openWriter
argument_list|(
name|ent
argument_list|,
name|superPackageName
argument_list|,
name|superClassName
argument_list|)
decl_stmt|;
if|if
condition|(
name|superOut
operator|!=
literal|null
condition|)
block|{
name|superGenSetup
operator|.
name|generateClass
argument_list|(
name|superOut
argument_list|,
name|dataMap
argument_list|,
name|ent
argument_list|,
name|fqnBaseClass
argument_list|,
name|fqnSuperClass
argument_list|,
name|fqnSubClass
argument_list|)
expr_stmt|;
name|superOut
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|Writer
name|mainOut
init|=
name|openWriter
argument_list|(
name|ent
argument_list|,
name|subPackageName
argument_list|,
name|subClassName
argument_list|)
decl_stmt|;
if|if
condition|(
name|mainOut
operator|!=
literal|null
condition|)
block|{
name|mainGenSetup
operator|.
name|generateClass
argument_list|(
name|mainOut
argument_list|,
name|dataMap
argument_list|,
name|ent
argument_list|,
name|fqnBaseClass
argument_list|,
name|fqnSuperClass
argument_list|,
name|fqnSubClass
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
specifier|protected
name|Collection
argument_list|<
name|ObjEntity
argument_list|>
name|entitiesForCurrentMode
parameter_list|()
block|{
comment|// TODO: andrus, 12/2/2007 - should we setup a dummy entity for an empty map in
comment|// DataMap mode?
if|if
condition|(
name|mode
operator|!=
name|ClassGeneratorMode
operator|.
name|entity
operator|&&
operator|!
name|entities
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|Collections
operator|.
name|singleton
argument_list|(
name|entities
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|this
operator|.
name|entities
return|;
block|}
block|}
comment|/**      * Runs class generation. Produces a single Java class for each ObjEntity in the map.      */
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
name|TemplateProcessor
name|generator
init|=
operator|new
name|TemplateProcessor
argument_list|(
name|classTemplate
argument_list|,
name|context
argument_list|)
decl_stmt|;
for|for
control|(
name|ObjEntity
name|ent
range|:
name|entitiesForCurrentMode
argument_list|()
control|)
block|{
name|String
name|fqnSubClass
init|=
name|ent
operator|.
name|getClassName
argument_list|()
decl_stmt|;
name|String
name|fqnBaseClass
init|=
operator|(
literal|null
operator|!=
name|ent
operator|.
name|getSuperClassName
argument_list|()
operator|)
condition|?
name|ent
operator|.
name|getSuperClassName
argument_list|()
else|:
name|CayenneDataObject
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
name|StringUtils
name|stringUtils
init|=
name|StringUtils
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|String
name|subClassName
init|=
name|stringUtils
operator|.
name|stripPackageName
argument_list|(
name|fqnSubClass
argument_list|)
decl_stmt|;
name|String
name|subPackageName
init|=
name|stringUtils
operator|.
name|stripClass
argument_list|(
name|fqnSubClass
argument_list|)
decl_stmt|;
name|String
name|superClassName
init|=
name|superPrefix
operator|+
name|stringUtils
operator|.
name|stripPackageName
argument_list|(
name|fqnSubClass
argument_list|)
decl_stmt|;
name|String
name|superPackageName
init|=
name|this
operator|.
name|superPkg
decl_stmt|;
name|String
name|fqnSuperClass
init|=
name|superPackageName
operator|+
literal|"."
operator|+
name|superClassName
decl_stmt|;
name|Writer
name|out
init|=
name|openWriter
argument_list|(
name|ent
argument_list|,
name|subPackageName
argument_list|,
name|subClassName
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
name|ent
argument_list|,
name|fqnBaseClass
argument_list|,
name|fqnSuperClass
argument_list|,
name|fqnSubClass
argument_list|)
expr_stmt|;
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Runs class generation.      */
specifier|public
name|void
name|execute
parameter_list|()
throws|throws
name|Exception
block|{
name|validateAttributes
argument_list|()
expr_stmt|;
if|if
condition|(
name|makePairs
condition|)
block|{
name|String
name|t
init|=
name|getTemplateForPairs
argument_list|()
decl_stmt|;
name|String
name|st
init|=
name|getSupertemplateForPairs
argument_list|()
decl_stmt|;
name|generateClassPairs
argument_list|(
name|t
argument_list|,
name|st
argument_list|,
name|SUPERCLASS_PREFIX
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|generateSingleClasses
argument_list|(
name|getTemplateForSingles
argument_list|()
argument_list|,
name|SUPERCLASS_PREFIX
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Validates the state of this class generator. Throws CayenneRuntimeException if it      * is in an inconsistent state. Called internally from "execute".      */
specifier|protected
name|void
name|validateAttributes
parameter_list|()
block|{
if|if
condition|(
name|destDir
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"'destDir' attribute is missing."
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|destDir
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"'destDir' is not a directory."
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|destDir
operator|.
name|canWrite
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Do not have write permissions for "
operator|+
name|destDir
argument_list|)
throw|;
block|}
block|}
comment|/**      * Sets the destDir.      */
specifier|public
name|void
name|setDestDir
parameter_list|(
name|File
name|destDir
parameter_list|)
block|{
name|this
operator|.
name|destDir
operator|=
name|destDir
expr_stmt|;
block|}
comment|/**      * Sets<code>overwrite</code> property.      */
specifier|public
name|void
name|setOverwrite
parameter_list|(
name|boolean
name|overwrite
parameter_list|)
block|{
name|this
operator|.
name|overwrite
operator|=
name|overwrite
expr_stmt|;
block|}
comment|/**      * Sets<code>makepairs</code> property.      */
specifier|public
name|void
name|setMakePairs
parameter_list|(
name|boolean
name|makePairs
parameter_list|)
block|{
name|this
operator|.
name|makePairs
operator|=
name|makePairs
expr_stmt|;
block|}
comment|/**      * Sets<code>template</code> property.      */
specifier|public
name|void
name|setTemplate
parameter_list|(
name|String
name|template
parameter_list|)
block|{
name|this
operator|.
name|template
operator|=
name|template
expr_stmt|;
block|}
comment|/**      * Sets<code>superTemplate</code> property.      */
specifier|public
name|void
name|setSuperTemplate
parameter_list|(
name|String
name|superTemplate
parameter_list|)
block|{
name|this
operator|.
name|superTemplate
operator|=
name|superTemplate
expr_stmt|;
block|}
comment|/**      * Sets<code>usepkgpath</code> property.      */
specifier|public
name|void
name|setUsePkgPath
parameter_list|(
name|boolean
name|usePkgPath
parameter_list|)
block|{
name|this
operator|.
name|usePkgPath
operator|=
name|usePkgPath
expr_stmt|;
block|}
comment|/**      * Sets<code>outputPattern</code> property.      */
specifier|public
name|void
name|setOutputPattern
parameter_list|(
name|String
name|outputPattern
parameter_list|)
block|{
name|this
operator|.
name|outputPattern
operator|=
name|outputPattern
expr_stmt|;
block|}
comment|/**      * Opens a Writer to write generated output. Writer encoding is determined from the      * value of the "encoding" property.      */
specifier|protected
name|Writer
name|openWriter
parameter_list|(
name|ObjEntity
name|entity
parameter_list|,
name|String
name|pkgName
parameter_list|,
name|String
name|className
parameter_list|)
throws|throws
name|Exception
block|{
name|boolean
name|superclass
init|=
name|className
operator|.
name|startsWith
argument_list|(
name|SUPERCLASS_PREFIX
argument_list|)
decl_stmt|;
name|File
name|outFile
init|=
operator|(
name|superclass
operator|)
condition|?
name|fileForSuperclass
argument_list|(
name|pkgName
argument_list|,
name|className
argument_list|)
else|:
name|fileForClass
argument_list|(
name|pkgName
argument_list|,
name|className
argument_list|)
decl_stmt|;
if|if
condition|(
name|outFile
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
name|logger
operator|!=
literal|null
condition|)
block|{
name|String
name|label
init|=
name|superclass
condition|?
literal|"superclass"
else|:
literal|"class"
decl_stmt|;
name|logger
operator|.
name|info
argument_list|(
literal|"Generating "
operator|+
name|label
operator|+
literal|" file: "
operator|+
name|outFile
operator|.
name|getCanonicalPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// return writer with specified encoding
name|FileOutputStream
name|out
init|=
operator|new
name|FileOutputStream
argument_list|(
name|outFile
argument_list|)
decl_stmt|;
return|return
operator|(
name|getEncoding
argument_list|()
operator|!=
literal|null
operator|)
condition|?
operator|new
name|OutputStreamWriter
argument_list|(
name|out
argument_list|,
name|getEncoding
argument_list|()
argument_list|)
else|:
operator|new
name|OutputStreamWriter
argument_list|(
name|out
argument_list|)
return|;
block|}
comment|/**      * Returns a target file where a generated superclass must be saved. If null is      * returned, class shouldn't be generated.      */
specifier|protected
name|File
name|fileForSuperclass
parameter_list|(
name|String
name|pkgName
parameter_list|,
name|String
name|className
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|filename
init|=
name|NamePatternMatcher
operator|.
name|replaceWildcardInStringWithString
argument_list|(
name|WILDCARD
argument_list|,
name|outputPattern
argument_list|,
name|className
argument_list|)
decl_stmt|;
name|File
name|dest
init|=
operator|new
name|File
argument_list|(
name|mkpath
argument_list|(
name|destDir
argument_list|,
name|pkgName
argument_list|)
argument_list|,
name|filename
argument_list|)
decl_stmt|;
comment|// Ignore if the destination is newer than the map
comment|// (internal timestamp), i.e. has been generated after the map was
comment|// last saved AND the template is older than the destination file
if|if
condition|(
name|dest
operator|.
name|exists
argument_list|()
operator|&&
operator|!
name|isOld
argument_list|(
name|dest
argument_list|)
condition|)
block|{
if|if
condition|(
name|superTemplate
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|File
name|superTemplateFile
init|=
operator|new
name|File
argument_list|(
name|superTemplate
argument_list|)
decl_stmt|;
if|if
condition|(
name|superTemplateFile
operator|.
name|lastModified
argument_list|()
operator|<
name|dest
operator|.
name|lastModified
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
block|}
return|return
name|dest
return|;
block|}
comment|/**      * Returns a target file where a generated class must be saved. If null is returned,      * class shouldn't be generated.      */
specifier|protected
name|File
name|fileForClass
parameter_list|(
name|String
name|pkgName
parameter_list|,
name|String
name|className
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|filename
init|=
name|NamePatternMatcher
operator|.
name|replaceWildcardInStringWithString
argument_list|(
name|WILDCARD
argument_list|,
name|outputPattern
argument_list|,
name|className
argument_list|)
decl_stmt|;
name|File
name|dest
init|=
operator|new
name|File
argument_list|(
name|mkpath
argument_list|(
name|destDir
argument_list|,
name|pkgName
argument_list|)
argument_list|,
name|filename
argument_list|)
decl_stmt|;
if|if
condition|(
name|dest
operator|.
name|exists
argument_list|()
condition|)
block|{
comment|// no overwrite of subclasses
if|if
condition|(
name|makePairs
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|// skip if said so
if|if
condition|(
operator|!
name|overwrite
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|// Ignore if the destination is newer than the map
comment|// (internal timestamp), i.e. has been generated after the map was
comment|// last saved AND the template is older than the destination file
if|if
condition|(
operator|!
name|isOld
argument_list|(
name|dest
argument_list|)
condition|)
block|{
if|if
condition|(
name|template
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|File
name|templateFile
init|=
operator|new
name|File
argument_list|(
name|template
argument_list|)
decl_stmt|;
if|if
condition|(
name|templateFile
operator|.
name|lastModified
argument_list|()
operator|<
name|dest
operator|.
name|lastModified
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
return|return
name|dest
return|;
block|}
comment|/**      * Returns true if<code>file</code> parameter is older than internal timestamp of      * this class generator.      */
specifier|protected
name|boolean
name|isOld
parameter_list|(
name|File
name|file
parameter_list|)
block|{
return|return
name|file
operator|.
name|lastModified
argument_list|()
operator|<=
name|getTimestamp
argument_list|()
return|;
block|}
comment|/**      * Returns a File object corresponding to a directory where files that belong to      *<code>pkgName</code> package should reside. Creates any missing diectories below      *<code>dest</code>.      */
specifier|protected
name|File
name|mkpath
parameter_list|(
name|File
name|dest
parameter_list|,
name|String
name|pkgName
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|usePkgPath
operator|||
name|pkgName
operator|==
literal|null
condition|)
block|{
return|return
name|dest
return|;
block|}
name|String
name|path
init|=
name|pkgName
operator|.
name|replace
argument_list|(
literal|'.'
argument_list|,
name|File
operator|.
name|separatorChar
argument_list|)
decl_stmt|;
name|File
name|fullPath
init|=
operator|new
name|File
argument_list|(
name|dest
argument_list|,
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|fullPath
operator|.
name|isDirectory
argument_list|()
operator|&&
operator|!
name|fullPath
operator|.
name|mkdirs
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
literal|"Error making path: "
operator|+
name|fullPath
argument_list|)
throw|;
block|}
return|return
name|fullPath
return|;
block|}
comment|/**      * Returns template file path for Java class when generating single classes.      */
specifier|protected
name|String
name|getTemplateForSingles
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|(
name|template
operator|!=
literal|null
operator|)
condition|?
name|template
else|:
name|defaultSingleClassTemplate
argument_list|()
return|;
block|}
comment|/**      * Returns template file path for Java subclass when generating class pairs.      */
specifier|protected
name|String
name|getTemplateForPairs
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|(
name|template
operator|!=
literal|null
operator|)
condition|?
name|template
else|:
name|defaultSubclassTemplate
argument_list|()
return|;
block|}
comment|/**      * Returns template file path for Java superclass when generating class pairs.      */
specifier|protected
name|String
name|getSupertemplateForPairs
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|(
name|superTemplate
operator|!=
literal|null
operator|)
condition|?
name|superTemplate
else|:
name|defaultSuperclassTemplate
argument_list|()
return|;
block|}
comment|/**      * Returns internal timestamp of this generator used to make decisions about      * overwriting individual files.      */
specifier|public
name|long
name|getTimestamp
parameter_list|()
block|{
return|return
name|timestamp
return|;
block|}
specifier|public
name|void
name|setTimestamp
parameter_list|(
name|long
name|timestamp
parameter_list|)
block|{
name|this
operator|.
name|timestamp
operator|=
name|timestamp
expr_stmt|;
block|}
comment|/**      * Returns file encoding for the generated files.      */
specifier|public
name|String
name|getEncoding
parameter_list|()
block|{
return|return
name|encoding
return|;
block|}
comment|/**      * Sets file encoding. If set to null, default system encoding will be used.      */
specifier|public
name|void
name|setEncoding
parameter_list|(
name|String
name|encoding
parameter_list|)
block|{
name|this
operator|.
name|encoding
operator|=
name|encoding
expr_stmt|;
block|}
comment|/**      * Returns "superPkg" property value - a name of a superclass package that should be      * used for all generated superclasses.      */
specifier|public
name|String
name|getSuperPkg
parameter_list|()
block|{
return|return
name|superPkg
return|;
block|}
comment|/**      * Sets "superPkg" property value.      */
specifier|public
name|void
name|setSuperPkg
parameter_list|(
name|String
name|superPkg
parameter_list|)
block|{
name|this
operator|.
name|superPkg
operator|=
name|superPkg
expr_stmt|;
block|}
comment|/**      * @return Returns the dataMap.      */
specifier|public
name|DataMap
name|getDataMap
parameter_list|()
block|{
return|return
name|dataMap
return|;
block|}
comment|/**      * @param dataMap The dataMap to set.      */
specifier|public
name|void
name|setDataMap
parameter_list|(
name|DataMap
name|dataMap
parameter_list|)
block|{
name|this
operator|.
name|dataMap
operator|=
name|dataMap
expr_stmt|;
block|}
specifier|public
name|Collection
argument_list|<
name|ObjEntity
argument_list|>
name|getEntities
parameter_list|()
block|{
return|return
name|entities
return|;
block|}
comment|/**      * Initializes internal ObjEntities list. This method creates a copy of the provided      * list to allow its independent modification and also filters out entities that do      * not require class generation.      */
specifier|public
name|void
name|setEntities
parameter_list|(
name|Collection
argument_list|<
name|ObjEntity
argument_list|>
name|objEntities
parameter_list|)
block|{
name|this
operator|.
name|entities
operator|=
name|objEntities
operator|!=
literal|null
condition|?
operator|new
name|ArrayList
argument_list|<
name|ObjEntity
argument_list|>
argument_list|(
name|objEntities
argument_list|)
else|:
operator|new
name|ArrayList
argument_list|<
name|ObjEntity
argument_list|>
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|setMode
parameter_list|(
name|ClassGeneratorMode
name|mode
parameter_list|)
block|{
name|this
operator|.
name|mode
operator|=
name|mode
expr_stmt|;
block|}
comment|/**      * Sets an optional shared VelocityContext. Useful with tools like VPP that can set      * custom values in the context, not known to Cayenne.      */
specifier|public
name|void
name|setContext
parameter_list|(
name|VelocityContext
name|context
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
block|}
comment|/**      * Injects an optional logger that will be used to trace generated files at the info      * level.      */
specifier|public
name|void
name|setLogger
parameter_list|(
name|Log
name|logger
parameter_list|)
block|{
name|this
operator|.
name|logger
operator|=
name|logger
expr_stmt|;
block|}
block|}
end_class

end_unit

