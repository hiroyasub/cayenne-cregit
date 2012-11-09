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
name|dbimport
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

begin_comment
comment|/**  * @since 3.2  */
end_comment

begin_class
specifier|public
class|class
name|DbImportParameters
block|{
comment|/**      * DataMap XML file to use as a base for DB importing.      */
specifier|private
name|File
name|map
decl_stmt|;
comment|/**      * A default package for ObjEntity Java classes.      */
specifier|private
name|String
name|defaultPackage
decl_stmt|;
comment|/**      * Indicates that the old mapping should be completely removed and replaced      * with the new data based on reverse engineering.      */
specifier|private
name|boolean
name|overwrite
decl_stmt|;
comment|/**      * DB schema to use for DB importing.      */
specifier|private
name|String
name|catalog
decl_stmt|;
comment|/**      * DB schema to use for DB importing.      */
specifier|private
name|String
name|schema
decl_stmt|;
comment|/**      * Pattern for tables to import from DB      */
specifier|private
name|String
name|tablePattern
decl_stmt|;
comment|/**      * Indicates whether stored procedures should be imported.      */
specifier|private
name|boolean
name|importProcedures
decl_stmt|;
comment|/**      * Pattern for stored procedures to import from DB. This is only meaningful      * if<code>importProcedures</code> is set to<code>true</code>.      */
specifier|private
name|String
name|procedurePattern
decl_stmt|;
comment|/**      * Indicates whether primary keys should be mapped as meaningful attributes      * in the object entities.      */
specifier|private
name|boolean
name|meaningfulPk
decl_stmt|;
comment|/**      * Java class implementing org.apache.cayenne.map.naming.NamingStrategy.      * This is used to specify how ObjEntities will be mapped from the imported      * DB schema.      */
specifier|private
name|String
name|namingStrategy
decl_stmt|;
comment|/**      * Java class implementing org.apache.cayenne.dba.DbAdapter. This attribute      * is optional, the default is AutoAdapter, i.e. Cayenne would try to guess      * the DB type.      */
specifier|private
name|String
name|adapter
decl_stmt|;
comment|/**      * A class of JDBC driver to use for the target database.      */
specifier|private
name|String
name|driver
decl_stmt|;
comment|/**      * JDBC connection URL of a target database.      */
specifier|private
name|String
name|url
decl_stmt|;
comment|/**      * Database user name.      */
specifier|private
name|String
name|username
decl_stmt|;
comment|/**      * Database user password.      */
specifier|private
name|String
name|password
decl_stmt|;
specifier|public
name|File
name|getMap
parameter_list|()
block|{
return|return
name|map
return|;
block|}
specifier|public
name|void
name|setMap
parameter_list|(
name|File
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
specifier|public
name|String
name|getDefaultPackage
parameter_list|()
block|{
return|return
name|defaultPackage
return|;
block|}
specifier|public
name|void
name|setDefaultPackage
parameter_list|(
name|String
name|defaultPackage
parameter_list|)
block|{
name|this
operator|.
name|defaultPackage
operator|=
name|defaultPackage
expr_stmt|;
block|}
specifier|public
name|boolean
name|isOverwrite
parameter_list|()
block|{
return|return
name|overwrite
return|;
block|}
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
specifier|public
name|String
name|getCatalog
parameter_list|()
block|{
return|return
name|catalog
return|;
block|}
specifier|public
name|void
name|setCatalog
parameter_list|(
name|String
name|catalog
parameter_list|)
block|{
name|this
operator|.
name|catalog
operator|=
name|catalog
expr_stmt|;
block|}
specifier|public
name|String
name|getSchema
parameter_list|()
block|{
return|return
name|schema
return|;
block|}
specifier|public
name|void
name|setSchema
parameter_list|(
name|String
name|schema
parameter_list|)
block|{
name|this
operator|.
name|schema
operator|=
name|schema
expr_stmt|;
block|}
specifier|public
name|String
name|getTablePattern
parameter_list|()
block|{
return|return
name|tablePattern
return|;
block|}
specifier|public
name|void
name|setTablePattern
parameter_list|(
name|String
name|tablePattern
parameter_list|)
block|{
name|this
operator|.
name|tablePattern
operator|=
name|tablePattern
expr_stmt|;
block|}
specifier|public
name|boolean
name|isImportProcedures
parameter_list|()
block|{
return|return
name|importProcedures
return|;
block|}
specifier|public
name|void
name|setImportProcedures
parameter_list|(
name|boolean
name|importProcedures
parameter_list|)
block|{
name|this
operator|.
name|importProcedures
operator|=
name|importProcedures
expr_stmt|;
block|}
specifier|public
name|String
name|getProcedurePattern
parameter_list|()
block|{
return|return
name|procedurePattern
return|;
block|}
specifier|public
name|void
name|setProcedurePattern
parameter_list|(
name|String
name|procedurePattern
parameter_list|)
block|{
name|this
operator|.
name|procedurePattern
operator|=
name|procedurePattern
expr_stmt|;
block|}
specifier|public
name|boolean
name|isMeaningfulPk
parameter_list|()
block|{
return|return
name|meaningfulPk
return|;
block|}
specifier|public
name|void
name|setMeaningfulPk
parameter_list|(
name|boolean
name|meaningfulPk
parameter_list|)
block|{
name|this
operator|.
name|meaningfulPk
operator|=
name|meaningfulPk
expr_stmt|;
block|}
specifier|public
name|String
name|getNamingStrategy
parameter_list|()
block|{
return|return
name|namingStrategy
return|;
block|}
specifier|public
name|void
name|setNamingStrategy
parameter_list|(
name|String
name|namingStrategy
parameter_list|)
block|{
name|this
operator|.
name|namingStrategy
operator|=
name|namingStrategy
expr_stmt|;
block|}
specifier|public
name|String
name|getAdapter
parameter_list|()
block|{
return|return
name|adapter
return|;
block|}
specifier|public
name|void
name|setAdapter
parameter_list|(
name|String
name|adapter
parameter_list|)
block|{
name|this
operator|.
name|adapter
operator|=
name|adapter
expr_stmt|;
block|}
specifier|public
name|String
name|getDriver
parameter_list|()
block|{
return|return
name|driver
return|;
block|}
specifier|public
name|void
name|setDriver
parameter_list|(
name|String
name|driver
parameter_list|)
block|{
name|this
operator|.
name|driver
operator|=
name|driver
expr_stmt|;
block|}
specifier|public
name|String
name|getUrl
parameter_list|()
block|{
return|return
name|url
return|;
block|}
specifier|public
name|void
name|setUrl
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|this
operator|.
name|url
operator|=
name|url
expr_stmt|;
block|}
specifier|public
name|String
name|getUsername
parameter_list|()
block|{
return|return
name|username
return|;
block|}
specifier|public
name|void
name|setUsername
parameter_list|(
name|String
name|username
parameter_list|)
block|{
name|this
operator|.
name|username
operator|=
name|username
expr_stmt|;
block|}
specifier|public
name|String
name|getPassword
parameter_list|()
block|{
return|return
name|password
return|;
block|}
specifier|public
name|void
name|setPassword
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|this
operator|.
name|password
operator|=
name|password
expr_stmt|;
block|}
block|}
end_class

end_unit
