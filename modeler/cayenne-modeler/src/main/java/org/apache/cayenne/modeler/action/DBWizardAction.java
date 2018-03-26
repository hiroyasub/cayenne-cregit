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
name|action
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Connection
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
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JOptionPane
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
name|dbload
operator|.
name|DbLoader
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
name|dialog
operator|.
name|db
operator|.
name|DataSourceWizard
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
name|DbActionOptionsDialog
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
name|CayenneAction
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|DBWizardAction
parameter_list|<
name|T
extends|extends
name|DbActionOptionsDialog
parameter_list|>
extends|extends
name|CayenneAction
block|{
specifier|private
specifier|static
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|DBWizardAction
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|DBWizardAction
parameter_list|(
name|String
name|name
parameter_list|,
name|Application
name|application
parameter_list|)
block|{
name|super
argument_list|(
name|name
argument_list|,
name|application
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|DataSourceWizard
name|dataSourceWizardDialog
parameter_list|(
name|String
name|title
parameter_list|)
block|{
comment|// connect
name|DataSourceWizard
name|connectWizard
init|=
operator|new
name|DataSourceWizard
argument_list|(
name|getProjectController
argument_list|()
argument_list|,
name|title
argument_list|)
decl_stmt|;
name|connectWizard
operator|.
name|setProjectController
argument_list|(
name|getProjectController
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|connectWizard
operator|.
name|startupAction
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|connectWizard
return|;
block|}
specifier|protected
specifier|abstract
name|T
name|createDialog
parameter_list|(
name|Collection
argument_list|<
name|String
argument_list|>
name|catalogs
parameter_list|,
name|Collection
argument_list|<
name|String
argument_list|>
name|schemas
parameter_list|,
name|String
name|currentCatalog
parameter_list|,
name|String
name|currentSchema
parameter_list|,
name|int
name|command
parameter_list|)
function_decl|;
specifier|protected
name|T
name|loaderOptionDialog
parameter_list|(
name|DataSourceWizard
name|connectWizard
parameter_list|)
block|{
comment|// use this catalog as the default...
name|List
argument_list|<
name|String
argument_list|>
name|catalogs
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|schemas
decl_stmt|;
name|String
name|currentCatalog
decl_stmt|;
name|String
name|currentSchema
init|=
literal|null
decl_stmt|;
try|try
init|(
name|Connection
name|connection
init|=
name|connectWizard
operator|.
name|getDataSource
argument_list|()
operator|.
name|getConnection
argument_list|()
init|)
block|{
name|catalogs
operator|=
name|getCatalogs
argument_list|(
name|connectWizard
argument_list|,
name|connection
argument_list|)
expr_stmt|;
name|schemas
operator|=
name|getSchemas
argument_list|(
name|connection
argument_list|)
expr_stmt|;
if|if
condition|(
name|catalogs
operator|.
name|isEmpty
argument_list|()
operator|&&
name|schemas
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|currentCatalog
operator|=
name|connection
operator|.
name|getCatalog
argument_list|()
expr_stmt|;
try|try
block|{
name|currentSchema
operator|=
name|connection
operator|.
name|getSchema
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|th
parameter_list|)
block|{
name|LOGGER
operator|.
name|warn
argument_list|(
literal|"Error getting schema."
argument_list|,
name|th
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|JOptionPane
operator|.
name|showMessageDialog
argument_list|(
name|Application
operator|.
name|getFrame
argument_list|()
argument_list|,
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|,
literal|"Error loading schemas dialog"
argument_list|,
name|JOptionPane
operator|.
name|ERROR_MESSAGE
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
name|T
name|optionsDialog
init|=
name|getStartDialog
argument_list|(
name|catalogs
argument_list|,
name|schemas
argument_list|,
name|currentCatalog
argument_list|,
name|currentSchema
argument_list|)
decl_stmt|;
name|optionsDialog
operator|.
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
while|while
condition|(
operator|(
name|optionsDialog
operator|.
name|getChoice
argument_list|()
operator|!=
name|DbActionOptionsDialog
operator|.
name|CANCEL
operator|)
condition|)
block|{
if|if
condition|(
name|optionsDialog
operator|.
name|getChoice
argument_list|()
operator|==
name|DbActionOptionsDialog
operator|.
name|SELECT
condition|)
block|{
return|return
name|optionsDialog
return|;
block|}
name|optionsDialog
operator|=
name|createDialog
argument_list|(
name|catalogs
argument_list|,
name|schemas
argument_list|,
name|currentCatalog
argument_list|,
name|currentSchema
argument_list|,
name|optionsDialog
operator|.
name|getChoice
argument_list|()
argument_list|)
expr_stmt|;
name|optionsDialog
operator|.
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|T
name|getStartDialog
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|catalogs
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|schemas
parameter_list|,
name|String
name|currentCatalog
parameter_list|,
name|String
name|currentSchema
parameter_list|)
block|{
name|int
name|command
init|=
name|DbActionOptionsDialog
operator|.
name|SELECT
decl_stmt|;
return|return
name|createDialog
argument_list|(
name|catalogs
argument_list|,
name|schemas
argument_list|,
name|currentCatalog
argument_list|,
name|currentSchema
argument_list|,
name|command
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|getCatalogs
parameter_list|(
name|DataSourceWizard
name|connectWizard
parameter_list|,
name|Connection
name|connection
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|connectWizard
operator|.
name|getAdapter
argument_list|()
operator|.
name|supportsCatalogsOnReverseEngineering
argument_list|()
condition|)
block|{
return|return
operator|(
name|List
argument_list|<
name|String
argument_list|>
operator|)
name|Collections
operator|.
name|EMPTY_LIST
return|;
block|}
return|return
name|DbLoader
operator|.
name|loadCatalogs
argument_list|(
name|connection
argument_list|)
return|;
block|}
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|getSchemas
parameter_list|(
name|Connection
name|connection
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|DbLoader
operator|.
name|loadSchemas
argument_list|(
name|connection
argument_list|)
return|;
block|}
block|}
end_class

end_unit

