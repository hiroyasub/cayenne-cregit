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
name|configuration
operator|.
name|xml
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
name|configuration
operator|.
name|DataMapLoader
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
name|di
operator|.
name|Inject
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
name|resource
operator|.
name|Resource
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
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|InputSource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|XMLReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_comment
comment|/**  * @since 3.1  * @since 4.1 moved from org.apache.cayenne.configuration package  */
end_comment

begin_class
specifier|public
class|class
name|XMLDataMapLoader
implements|implements
name|DataMapLoader
block|{
specifier|private
specifier|static
specifier|final
name|String
name|DATA_MAP_LOCATION_SUFFIX
init|=
literal|".map.xml"
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|HandlerFactory
name|handlerFactory
decl_stmt|;
specifier|private
name|DataMap
name|map
decl_stmt|;
specifier|public
specifier|synchronized
name|DataMap
name|load
parameter_list|(
name|Resource
name|configurationResource
parameter_list|)
throws|throws
name|CayenneRuntimeException
block|{
try|try
init|(
name|InputStream
name|in
init|=
name|configurationResource
operator|.
name|getURL
argument_list|()
operator|.
name|openStream
argument_list|()
init|)
block|{
name|XMLReader
name|parser
init|=
name|Util
operator|.
name|createXmlReader
argument_list|()
decl_stmt|;
name|LoaderContext
name|loaderContext
init|=
operator|new
name|LoaderContext
argument_list|(
name|parser
argument_list|,
name|handlerFactory
argument_list|)
decl_stmt|;
name|loaderContext
operator|.
name|addDataMapListener
argument_list|(
operator|new
name|DataMapLoaderListener
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onDataMapLoaded
parameter_list|(
name|DataMap
name|dataMap
parameter_list|)
block|{
name|map
operator|=
name|dataMap
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|RootDataMapHandler
name|rootHandler
init|=
operator|new
name|RootDataMapHandler
argument_list|(
name|loaderContext
argument_list|)
decl_stmt|;
name|parser
operator|.
name|setContentHandler
argument_list|(
name|rootHandler
argument_list|)
expr_stmt|;
name|parser
operator|.
name|setErrorHandler
argument_list|(
name|rootHandler
argument_list|)
expr_stmt|;
name|parser
operator|.
name|parse
argument_list|(
operator|new
name|InputSource
argument_list|(
name|in
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Error loading configuration from %s"
argument_list|,
name|e
argument_list|,
name|configurationResource
operator|.
name|getURL
argument_list|()
argument_list|)
throw|;
block|}
if|if
condition|(
name|map
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Unable to load data map from %s"
argument_list|,
name|configurationResource
operator|.
name|getURL
argument_list|()
argument_list|)
throw|;
block|}
if|if
condition|(
name|map
operator|.
name|getName
argument_list|()
operator|==
literal|null
condition|)
block|{
comment|// set name based on location if no name provided by map itself
name|map
operator|.
name|setName
argument_list|(
name|mapNameFromLocation
argument_list|(
name|configurationResource
operator|.
name|getURL
argument_list|()
operator|.
name|getFile
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|map
return|;
block|}
comment|/**      * Helper method to guess the map name from its location.      */
specifier|protected
name|String
name|mapNameFromLocation
parameter_list|(
name|String
name|location
parameter_list|)
block|{
if|if
condition|(
name|location
operator|==
literal|null
condition|)
block|{
return|return
literal|"Untitled"
return|;
block|}
name|int
name|lastSlash
init|=
name|location
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
decl_stmt|;
if|if
condition|(
name|lastSlash
operator|<
literal|0
condition|)
block|{
name|lastSlash
operator|=
name|location
operator|.
name|lastIndexOf
argument_list|(
literal|'\\'
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|lastSlash
operator|>=
literal|0
operator|&&
name|lastSlash
operator|+
literal|1
operator|<
name|location
operator|.
name|length
argument_list|()
condition|)
block|{
name|location
operator|=
name|location
operator|.
name|substring
argument_list|(
name|lastSlash
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|location
operator|.
name|endsWith
argument_list|(
name|DATA_MAP_LOCATION_SUFFIX
argument_list|)
condition|)
block|{
name|location
operator|=
name|location
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|location
operator|.
name|length
argument_list|()
operator|-
name|DATA_MAP_LOCATION_SUFFIX
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|location
return|;
block|}
specifier|public
name|void
name|setHandlerFactory
parameter_list|(
name|HandlerFactory
name|handlerFactory
parameter_list|)
block|{
name|this
operator|.
name|handlerFactory
operator|=
name|handlerFactory
expr_stmt|;
block|}
block|}
end_class

end_unit

