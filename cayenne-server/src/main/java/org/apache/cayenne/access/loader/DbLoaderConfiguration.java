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
name|access
operator|.
name|loader
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
name|access
operator|.
name|loader
operator|.
name|filters
operator|.
name|DbPath
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
name|loader
operator|.
name|filters
operator|.
name|EntityFilters
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
name|loader
operator|.
name|filters
operator|.
name|FilterFactory
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
name|loader
operator|.
name|filters
operator|.
name|FiltersConfig
import|;
end_import

begin_comment
comment|/**  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|DbLoaderConfiguration
block|{
comment|/**      * Returns a name of a generic class that should be used for all      * ObjEntities. The most common generic class is      * {@link org.apache.cayenne.CayenneDataObject}. If generic class name is      * null (which is the default), DbLoader will assign each entity a unique      * class name derived from the table name.      *      */
specifier|private
name|String
name|genericClassName
decl_stmt|;
comment|/*     // TODO: Andrus, 10/29/2005 - this type of filtering should be delegated to adapter     */
comment|/* TODO by default should skip name.startsWith("BIN$") */
comment|/*      private NameFilter tableFilter = NamePatternMatcher.build(null, null, "BIN$");      private NameFilter columnFilter;      private NameFilter proceduresFilter = new NameFilter() {         private final Collection<String> excludedProcedures = Arrays.asList(                 "auto_pk_for_table",                 "auto_pk_for_table;1" // the last name is some Mac OS X Sybase artifact         );          @Override         public boolean isIncluded(String string) {             return !excludedProcedures.contains(string);         }     }; */
comment|/**      * Java class implementing org.apache.cayenne.map.naming.NamingStrategy.      * This is used to specify how ObjEntities will be mapped from the imported      * DB schema.      */
specifier|private
name|String
name|namingStrategy
decl_stmt|;
specifier|private
name|String
index|[]
name|tableTypes
decl_stmt|;
specifier|private
name|FiltersConfig
name|filtersConfig
decl_stmt|;
specifier|public
name|String
name|getGenericClassName
parameter_list|()
block|{
return|return
name|genericClassName
return|;
block|}
specifier|public
name|void
name|setGenericClassName
parameter_list|(
name|String
name|genericClassName
parameter_list|)
block|{
name|this
operator|.
name|genericClassName
operator|=
name|genericClassName
expr_stmt|;
block|}
specifier|public
name|String
index|[]
name|getTableTypes
parameter_list|()
block|{
return|return
name|tableTypes
return|;
block|}
specifier|public
name|void
name|setTableTypes
parameter_list|(
name|String
index|[]
name|tableTypes
parameter_list|)
block|{
name|this
operator|.
name|tableTypes
operator|=
name|tableTypes
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
name|FiltersConfig
name|getFiltersConfig
parameter_list|()
block|{
if|if
condition|(
name|filtersConfig
operator|==
literal|null
condition|)
block|{
comment|// this case is used often in tests where config not initialized properly
return|return
operator|new
name|FiltersConfig
argument_list|(
operator|new
name|EntityFilters
argument_list|(
operator|new
name|DbPath
argument_list|()
argument_list|,
name|FilterFactory
operator|.
name|TRUE
argument_list|,
name|FilterFactory
operator|.
name|TRUE
argument_list|,
name|FilterFactory
operator|.
name|TRUE
argument_list|)
argument_list|)
return|;
block|}
return|return
name|filtersConfig
return|;
block|}
specifier|public
name|void
name|setFiltersConfig
parameter_list|(
name|FiltersConfig
name|filtersConfig
parameter_list|)
block|{
name|this
operator|.
name|filtersConfig
operator|=
name|filtersConfig
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"EntitiesFilters: "
operator|+
name|getFiltersConfig
argument_list|()
return|;
block|}
block|}
end_class

end_unit

