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
name|event
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Constructor
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
name|Map
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
name|reflect
operator|.
name|PropertyUtils
import|;
end_import

begin_comment
comment|/**  * Factory to create JavaGroupsBridge instances. If JavaGroups library is not installed  * this factory will return a noop EventBridge as a failover mechanism.  *   * @since 1.1  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|JavaGroupsBridgeFactory
implements|implements
name|EventBridgeFactory
block|{
specifier|public
specifier|static
specifier|final
name|String
name|MCAST_ADDRESS_DEFAULT
init|=
literal|"228.0.0.5"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|MCAST_PORT_DEFAULT
init|=
literal|"22222"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|MCAST_ADDRESS_PROPERTY
init|=
literal|"cayenne.JavaGroupsBridge.mcast.address"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|MCAST_PORT_PROPERTY
init|=
literal|"cayenne.JavaGroupsBridge.mcast.port"
decl_stmt|;
comment|/**      * Defines a property for JavaGroups XML configuration file. Example file can be found      * at<a      * href="http://www.filip.net/javagroups/javagroups-protocol.xml">http://www.filip.net/javagroups/javagroups-protocol.xml</a>.      */
specifier|public
specifier|static
specifier|final
name|String
name|JGROUPS_CONFIG_URL_PROPERTY
init|=
literal|"javagroupsbridge.config.url"
decl_stmt|;
comment|/**      * Creates a JavaGroupsBridge instance. Since JavaGroups is not shipped with Cayenne      * and should be installed separately, a common misconfiguration problem may be the      * absense of JavaGroups jar file. This factory returns a dummy noop EventBridge, if      * this is the case. This would allow the application to continue to run, but without      * remote notifications.      */
specifier|public
name|EventBridge
name|createEventBridge
parameter_list|(
name|Collection
argument_list|<
name|EventSubject
argument_list|>
name|localSubjects
parameter_list|,
name|String
name|externalSubject
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|)
block|{
try|try
block|{
comment|// sniff JavaGroups presence
name|Class
operator|.
name|forName
argument_list|(
literal|"org.jgroups.Channel"
argument_list|)
expr_stmt|;
return|return
name|createJavaGroupsBridge
argument_list|(
name|localSubjects
argument_list|,
name|externalSubject
argument_list|,
name|properties
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|// recover from no JavaGroups
return|return
name|createNoopBridge
argument_list|()
return|;
block|}
block|}
specifier|private
name|EventBridge
name|createNoopBridge
parameter_list|()
block|{
return|return
operator|new
name|NoopEventBridge
argument_list|()
return|;
block|}
specifier|private
name|EventBridge
name|createJavaGroupsBridge
parameter_list|(
name|Collection
argument_list|<
name|EventSubject
argument_list|>
name|localSubjects
parameter_list|,
name|String
name|externalSubject
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|)
block|{
comment|// create JavaGroupsBridge using reflection to avoid triggering
comment|// ClassNotFound exceptions due to JavaGroups absence.
try|try
block|{
name|Constructor
argument_list|<
name|?
argument_list|>
name|c
init|=
name|Class
operator|.
name|forName
argument_list|(
literal|"org.apache.cayenne.event.JavaGroupsBridge"
argument_list|)
operator|.
name|getConstructor
argument_list|(
operator|new
name|Class
index|[]
block|{
name|Collection
operator|.
name|class
block|,
name|String
operator|.
name|class
block|}
argument_list|)
decl_stmt|;
name|Object
name|bridge
init|=
name|c
operator|.
name|newInstance
argument_list|(
operator|new
name|Object
index|[]
block|{
name|localSubjects
block|,
name|externalSubject
block|}
argument_list|)
decl_stmt|;
comment|// configure properties
name|String
name|multicastAddress
init|=
operator|(
name|String
operator|)
name|properties
operator|.
name|get
argument_list|(
name|MCAST_ADDRESS_PROPERTY
argument_list|)
decl_stmt|;
name|String
name|multicastPort
init|=
operator|(
name|String
operator|)
name|properties
operator|.
name|get
argument_list|(
name|MCAST_PORT_PROPERTY
argument_list|)
decl_stmt|;
name|String
name|configURL
init|=
operator|(
name|String
operator|)
name|properties
operator|.
name|get
argument_list|(
name|JGROUPS_CONFIG_URL_PROPERTY
argument_list|)
decl_stmt|;
name|PropertyUtils
operator|.
name|setProperty
argument_list|(
name|bridge
argument_list|,
literal|"configURL"
argument_list|,
name|configURL
argument_list|)
expr_stmt|;
name|PropertyUtils
operator|.
name|setProperty
argument_list|(
name|bridge
argument_list|,
literal|"multicastAddress"
argument_list|,
name|multicastAddress
operator|!=
literal|null
condition|?
name|multicastAddress
else|:
name|MCAST_ADDRESS_DEFAULT
argument_list|)
expr_stmt|;
name|PropertyUtils
operator|.
name|setProperty
argument_list|(
name|bridge
argument_list|,
literal|"multicastPort"
argument_list|,
name|multicastPort
operator|!=
literal|null
condition|?
name|multicastPort
else|:
name|MCAST_PORT_DEFAULT
argument_list|)
expr_stmt|;
return|return
operator|(
name|EventBridge
operator|)
name|bridge
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Error creating JavaGroupsBridge"
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
comment|// mockup EventBridge
class|class
name|NoopEventBridge
extends|extends
name|EventBridge
block|{
specifier|public
name|NoopEventBridge
parameter_list|()
block|{
name|super
argument_list|(
name|Collections
operator|.
name|EMPTY_SET
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|receivesExternalEvents
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|receivesLocalEvents
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|startupExternal
parameter_list|()
block|{
block|}
annotation|@
name|Override
specifier|protected
name|void
name|shutdownExternal
parameter_list|()
block|{
block|}
annotation|@
name|Override
specifier|protected
name|void
name|sendExternalEvent
parameter_list|(
name|CayenneEvent
name|localEvent
parameter_list|)
block|{
block|}
annotation|@
name|Override
specifier|public
name|void
name|startup
parameter_list|(
name|EventManager
name|eventManager
parameter_list|,
name|int
name|mode
parameter_list|,
name|Object
name|eventsSource
parameter_list|)
block|{
name|this
operator|.
name|eventManager
operator|=
name|eventManager
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|shutdown
parameter_list|()
block|{
block|}
block|}
block|}
end_class

end_unit

