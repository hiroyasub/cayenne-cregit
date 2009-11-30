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
name|pref
package|;
end_package

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
name|java
operator|.
name|util
operator|.
name|Timer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TimerTask
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
name|access
operator|.
name|DataContext
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
name|DataDomain
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
name|DataNode
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
name|DbGenerator
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
name|dba
operator|.
name|DbAdapter
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

begin_comment
comment|/**  * A Cayenne-based PreferenceService.  *   */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|CayennePreferenceService
implements|implements
name|PreferenceService
block|{
specifier|public
specifier|static
specifier|final
name|int
name|MIN_SAVE_INTERVAL
init|=
literal|500
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|DEFAULT_SAVE_INTERVAL
init|=
literal|20000
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SAVE_INTERVAL_KEY
init|=
literal|"saveInterval"
decl_stmt|;
specifier|protected
name|int
name|saveInterval
init|=
name|DEFAULT_SAVE_INTERVAL
decl_stmt|;
specifier|protected
name|Timer
name|saveTimer
decl_stmt|;
specifier|protected
name|DataContext
name|dataContext
decl_stmt|;
specifier|protected
name|String
name|defaultDomain
decl_stmt|;
specifier|public
name|CayennePreferenceService
parameter_list|(
name|String
name|defaultDomain
parameter_list|)
block|{
name|this
operator|.
name|defaultDomain
operator|=
name|defaultDomain
expr_stmt|;
block|}
specifier|public
name|DataContext
name|getDataContext
parameter_list|()
block|{
return|return
name|dataContext
return|;
block|}
specifier|public
name|void
name|setDataContext
parameter_list|(
name|DataContext
name|preferencesContext
parameter_list|)
block|{
name|this
operator|.
name|dataContext
operator|=
name|preferencesContext
expr_stmt|;
block|}
specifier|public
name|int
name|getSaveInterval
parameter_list|()
block|{
return|return
name|saveInterval
return|;
block|}
specifier|public
name|void
name|setSaveInterval
parameter_list|(
name|int
name|ms
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|saveInterval
operator|!=
name|ms
condition|)
block|{
name|this
operator|.
name|saveInterval
operator|=
name|ms
expr_stmt|;
comment|// save to preferences
name|getPreferenceDomain
argument_list|()
operator|.
name|getDetail
argument_list|(
name|SAVE_INTERVAL_KEY
argument_list|,
literal|true
argument_list|)
operator|.
name|setIntProperty
argument_list|(
name|SAVE_INTERVAL_KEY
argument_list|,
name|ms
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Returns a top-level domain.      */
specifier|public
name|Domain
name|getDomain
parameter_list|(
name|String
name|name
parameter_list|,
name|boolean
name|create
parameter_list|)
block|{
name|List
name|results
init|=
name|getDataContext
argument_list|()
operator|.
name|performQuery
argument_list|(
literal|"TopLevelDomain"
argument_list|,
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"name"
argument_list|,
name|name
argument_list|)
argument_list|,
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|results
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Found "
operator|+
name|results
operator|.
name|size
argument_list|()
operator|+
literal|" Domain objects for name '"
operator|+
name|name
operator|+
literal|"', only one expected."
argument_list|)
throw|;
block|}
if|if
condition|(
name|results
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
return|return
operator|(
name|Domain
operator|)
name|results
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
if|if
condition|(
operator|!
name|create
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Domain
name|domain
init|=
name|getDataContext
argument_list|()
operator|.
name|newObject
argument_list|(
name|Domain
operator|.
name|class
argument_list|)
decl_stmt|;
name|domain
operator|.
name|setLevel
argument_list|(
operator|new
name|Integer
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|domain
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|savePreferences
argument_list|()
expr_stmt|;
return|return
name|domain
return|;
block|}
comment|/**      * Configures service to run stopService on JVM shutdown.      */
specifier|public
name|void
name|stopOnShutdown
parameter_list|()
block|{
name|Thread
name|shutdown
init|=
operator|new
name|Thread
argument_list|(
literal|"CayennePrefrencesService Shutdown"
argument_list|)
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
name|stopService
argument_list|()
expr_stmt|;
block|}
block|}
decl_stmt|;
name|Runtime
operator|.
name|getRuntime
argument_list|()
operator|.
name|addShutdownHook
argument_list|(
name|shutdown
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|savePreferences
parameter_list|()
block|{
name|DataContext
name|context
init|=
name|this
operator|.
name|dataContext
decl_stmt|;
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
block|}
specifier|protected
name|Domain
name|getPreferenceDomain
parameter_list|()
block|{
name|Domain
name|defaultDomain
init|=
name|getDomain
argument_list|(
name|this
operator|.
name|defaultDomain
argument_list|,
literal|true
argument_list|)
decl_stmt|;
return|return
name|defaultDomain
operator|.
name|getSubdomain
argument_list|(
name|getClass
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Initializes service own prefrences.      */
specifier|protected
name|void
name|initPreferences
parameter_list|()
block|{
name|Domain
name|preferenceDomain
init|=
name|getPreferenceDomain
argument_list|()
decl_stmt|;
name|PreferenceDetail
name|saveInterval
init|=
name|preferenceDomain
operator|.
name|getDetail
argument_list|(
name|SAVE_INTERVAL_KEY
argument_list|,
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|saveInterval
operator|!=
literal|null
condition|)
block|{
name|setSaveInterval
argument_list|(
name|saveInterval
operator|.
name|getIntProperty
argument_list|(
name|SAVE_INTERVAL_KEY
argument_list|,
name|DEFAULT_SAVE_INTERVAL
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Helper method that provides an easy way for subclasses to create preferences schema      * on the fly.      */
specifier|protected
name|void
name|initSchema
parameter_list|()
block|{
name|DataDomain
name|domain
init|=
name|dataContext
operator|.
name|getParentDataDomain
argument_list|()
decl_stmt|;
for|for
control|(
name|DataMap
name|dataMap
range|:
name|domain
operator|.
name|getDataMaps
argument_list|()
control|)
block|{
name|DataMap
name|map
init|=
name|dataMap
decl_stmt|;
name|DataNode
name|node
init|=
name|domain
operator|.
name|lookupDataNode
argument_list|(
name|map
argument_list|)
decl_stmt|;
name|DbAdapter
name|adapter
init|=
name|node
operator|.
name|getAdapter
argument_list|()
decl_stmt|;
name|DbGenerator
name|generator
init|=
operator|new
name|DbGenerator
argument_list|(
name|adapter
argument_list|,
name|map
argument_list|)
decl_stmt|;
try|try
block|{
name|generator
operator|.
name|runGenerator
argument_list|(
name|node
operator|.
name|getDataSource
argument_list|()
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
literal|"Error creating preferences DB"
argument_list|,
name|th
argument_list|)
throw|;
block|}
block|}
block|}
comment|/**      * Starts preferences save timer.      */
specifier|protected
name|void
name|startTimer
parameter_list|()
block|{
name|TimerTask
name|saveTask
init|=
operator|new
name|TimerTask
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
name|savePreferences
argument_list|()
expr_stmt|;
block|}
block|}
decl_stmt|;
name|int
name|interval
init|=
operator|(
name|saveInterval
operator|>
name|MIN_SAVE_INTERVAL
operator|)
condition|?
name|saveInterval
else|:
name|MIN_SAVE_INTERVAL
decl_stmt|;
name|saveTimer
operator|=
operator|new
name|Timer
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|saveTimer
operator|.
name|schedule
argument_list|(
name|saveTask
argument_list|,
name|interval
argument_list|,
name|interval
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

