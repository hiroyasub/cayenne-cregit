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
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|filechooser
operator|.
name|FileFilter
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
name|conf
operator|.
name|Configuration
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
name|project
operator|.
name|DataMapFile
import|;
end_import

begin_comment
comment|/**  * A collection of common file filters used by CayenneModeler JFileChoosers.  *   * @since 1.1  */
end_comment

begin_class
specifier|public
class|class
name|FileFilters
block|{
specifier|protected
specifier|static
specifier|final
name|FileFilter
name|applicationFilter
init|=
operator|new
name|ApplicationFileFilter
argument_list|()
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|FileFilter
name|velotemplateFilter
init|=
operator|new
name|VelotemplateFileFilter
argument_list|()
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|FileFilter
name|eomodelFilter
init|=
operator|new
name|EOModelFileFilter
argument_list|()
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|FileFilter
name|eomodelSelectFilter
init|=
operator|new
name|EOModelSelectFilter
argument_list|()
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|FileFilter
name|dataMapFilter
init|=
operator|new
name|DataMapFileFilter
argument_list|()
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|FileFilter
name|classArchiveFilter
init|=
operator|new
name|JavaClassArchiveFilter
argument_list|()
decl_stmt|;
comment|/**      * Returns a FileFilter for java class archive files, such as JAR and ZIP.      */
specifier|public
specifier|static
name|FileFilter
name|getClassArchiveFilter
parameter_list|()
block|{
return|return
name|classArchiveFilter
return|;
block|}
comment|/**      * Returns a FileFilter used to select Cayenne Application project files.      */
specifier|public
specifier|static
name|FileFilter
name|getApplicationFilter
parameter_list|()
block|{
return|return
name|applicationFilter
return|;
block|}
comment|/**      * Returns a FileFilter used to select DataMap files.      */
specifier|public
specifier|static
name|FileFilter
name|getDataMapFilter
parameter_list|()
block|{
return|return
name|dataMapFilter
return|;
block|}
comment|/**      * Returns a FileFilter used to select Velocity template files.       * Filters files with ".vm" extension.      */
specifier|public
specifier|static
name|FileFilter
name|getVelotemplateFilter
parameter_list|()
block|{
return|return
name|velotemplateFilter
return|;
block|}
comment|/**      * Returns a FileFilter used to filter EOModels. This filter will only display      * directories and index.eomodeld files.      */
specifier|public
specifier|static
name|FileFilter
name|getEOModelFilter
parameter_list|()
block|{
return|return
name|eomodelFilter
return|;
block|}
comment|/**      * Returns FileFilter that defines the rules for EOModel selection.      * This filter will only allow selection of the following       * files/directories:      *<ul>      *<li>Directories with name matching<code>*.eomodeld</code>      *   that contain<code>index.eomodeld</code>.</li>      *<li><code>index.eomodeld</code> files contained within       *<code>*.eomodeld</code> directory.</li>      *</ul>      */
specifier|public
specifier|static
name|FileFilter
name|getEOModelSelectFilter
parameter_list|()
block|{
return|return
name|eomodelSelectFilter
return|;
block|}
specifier|static
specifier|final
class|class
name|JavaClassArchiveFilter
extends|extends
name|FileFilter
block|{
specifier|public
name|boolean
name|accept
parameter_list|(
name|File
name|f
parameter_list|)
block|{
if|if
condition|(
name|f
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
name|String
name|name
init|=
name|f
operator|.
name|getName
argument_list|()
operator|.
name|toLowerCase
argument_list|()
decl_stmt|;
return|return
operator|(
name|name
operator|.
name|length
argument_list|()
operator|>
literal|4
operator|&&
operator|(
name|name
operator|.
name|endsWith
argument_list|(
literal|".jar"
argument_list|)
operator|||
name|name
operator|.
name|endsWith
argument_list|(
literal|".zip"
argument_list|)
operator|)
operator|)
return|;
block|}
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
literal|"Java Class Archive (*.jar,*.zip)"
return|;
block|}
block|}
specifier|static
specifier|final
class|class
name|VelotemplateFileFilter
extends|extends
name|FileFilter
block|{
comment|/**          * Accepts all *.vm files.          */
specifier|public
name|boolean
name|accept
parameter_list|(
name|File
name|f
parameter_list|)
block|{
if|if
condition|(
name|f
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
name|String
name|name
init|=
name|f
operator|.
name|getName
argument_list|()
decl_stmt|;
return|return
operator|(
name|name
operator|.
name|endsWith
argument_list|(
literal|".vm"
argument_list|)
operator|&&
operator|!
name|name
operator|.
name|equals
argument_list|(
literal|".vm"
argument_list|)
operator|)
return|;
block|}
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
literal|"Velocity Templates (*.vm)"
return|;
block|}
block|}
specifier|static
specifier|final
class|class
name|ApplicationFileFilter
extends|extends
name|FileFilter
block|{
comment|/**          * Accepts all directories and all cayenne.xml files.          */
specifier|public
name|boolean
name|accept
parameter_list|(
name|File
name|f
parameter_list|)
block|{
return|return
name|f
operator|.
name|isDirectory
argument_list|()
operator|||
name|Configuration
operator|.
name|DEFAULT_DOMAIN_FILE
operator|.
name|equals
argument_list|(
name|f
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
comment|/**          *  Returns description of this filter.          */
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
literal|"Cayenne Applications ("
operator|+
name|Configuration
operator|.
name|DEFAULT_DOMAIN_FILE
operator|+
literal|")"
return|;
block|}
block|}
specifier|static
specifier|final
class|class
name|DataMapFileFilter
extends|extends
name|FileFilter
block|{
comment|/**          * Accepts all directories and all *.map.xml files.          */
specifier|public
name|boolean
name|accept
parameter_list|(
name|File
name|f
parameter_list|)
block|{
if|if
condition|(
name|f
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
name|String
name|name
init|=
name|f
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|.
name|endsWith
argument_list|(
name|DataMapFile
operator|.
name|LOCATION_SUFFIX
argument_list|)
operator|&&
operator|!
name|name
operator|.
name|equals
argument_list|(
name|DataMapFile
operator|.
name|LOCATION_SUFFIX
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
comment|/**          *  Returns description of this filter.          */
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
literal|"DataMaps (*"
operator|+
name|DataMapFile
operator|.
name|LOCATION_SUFFIX
operator|+
literal|")"
return|;
block|}
block|}
specifier|static
specifier|final
class|class
name|EOModelFileFilter
extends|extends
name|FileFilter
block|{
specifier|static
specifier|final
name|String
name|EOM_SUFFIX
init|=
literal|".eomodeld"
decl_stmt|;
specifier|static
specifier|final
name|String
name|EOM_INDEX
init|=
literal|"index"
operator|+
name|EOM_SUFFIX
decl_stmt|;
comment|/**          * Accepts all directories and<code>*.eomodeld/index.eomodeld</code> files.          */
specifier|public
name|boolean
name|accept
parameter_list|(
name|File
name|f
parameter_list|)
block|{
if|if
condition|(
name|f
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
name|File
name|parent
init|=
name|f
operator|.
name|getParentFile
argument_list|()
decl_stmt|;
return|return
name|parent
operator|!=
literal|null
operator|&&
name|parent
operator|.
name|getName
argument_list|()
operator|.
name|endsWith
argument_list|(
name|EOM_SUFFIX
argument_list|)
operator|&&
name|EOM_INDEX
operator|.
name|equals
argument_list|(
name|f
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
literal|"*"
operator|+
name|EOM_SUFFIX
return|;
block|}
block|}
specifier|static
specifier|final
class|class
name|EOModelSelectFilter
extends|extends
name|FileFilter
block|{
comment|/**          * Accepts all directories and<code>*.eomodeld/index.eomodeld</code> files.          *          * @see EOModelSelectFilter#accept(File)          */
specifier|public
name|boolean
name|accept
parameter_list|(
name|File
name|f
parameter_list|)
block|{
if|if
condition|(
name|f
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
if|if
condition|(
name|f
operator|.
name|getName
argument_list|()
operator|.
name|endsWith
argument_list|(
name|EOModelFileFilter
operator|.
name|EOM_SUFFIX
argument_list|)
operator|&&
operator|new
name|File
argument_list|(
name|f
argument_list|,
name|EOModelFileFilter
operator|.
name|EOM_INDEX
argument_list|)
operator|.
name|exists
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
if|else if
condition|(
name|f
operator|.
name|isFile
argument_list|()
condition|)
block|{
name|File
name|parent
init|=
name|f
operator|.
name|getParentFile
argument_list|()
decl_stmt|;
if|if
condition|(
name|parent
operator|!=
literal|null
operator|&&
name|parent
operator|.
name|getName
argument_list|()
operator|.
name|endsWith
argument_list|(
name|EOModelFileFilter
operator|.
name|EOM_SUFFIX
argument_list|)
operator|&&
name|EOModelFileFilter
operator|.
name|EOM_INDEX
operator|.
name|equals
argument_list|(
name|f
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
literal|"*"
operator|+
name|EOModelFileFilter
operator|.
name|EOM_SUFFIX
return|;
block|}
block|}
block|}
end_class

end_unit

