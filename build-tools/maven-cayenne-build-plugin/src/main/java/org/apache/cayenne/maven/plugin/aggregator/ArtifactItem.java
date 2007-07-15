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
name|maven
operator|.
name|plugin
operator|.
name|aggregator
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
comment|/**  * ArtifactItem represents information specified in the plugin configuration section for  * each artifact.  */
end_comment

begin_class
specifier|public
class|class
name|ArtifactItem
block|{
comment|/**      * Group Id of Artifact      *       * @parameter      * @required      */
specifier|private
name|String
name|groupId
decl_stmt|;
comment|/**      * Name of Artifact      *       * @parameter      * @required      */
specifier|private
name|String
name|artifactId
decl_stmt|;
comment|/**      * Version of Artifact      *       * @parameter      */
specifier|private
name|String
name|version
init|=
literal|null
decl_stmt|;
comment|/**      * Type of Artifact (War,Jar,etc)      *       * @parameter      * @required      */
specifier|private
name|String
name|type
init|=
literal|"jar"
decl_stmt|;
comment|/**      * Classifier for Artifact (tests,sources,etc)      *       * @parameter      */
specifier|private
name|String
name|classifier
decl_stmt|;
comment|/**      * Location to use for this Artifact. Overrides default location.      *       * @parameter      */
specifier|private
name|File
name|outputDirectory
decl_stmt|;
comment|/**      * Provides ability to change destination file name      *       * @parameter      */
specifier|private
name|String
name|destFileName
decl_stmt|;
comment|/**      * @return Returns the artifactId.      */
specifier|public
name|String
name|getArtifactId
parameter_list|()
block|{
return|return
name|artifactId
return|;
block|}
comment|/**      * @param artifactId The artifactId to set.      */
specifier|public
name|void
name|setArtifactId
parameter_list|(
name|String
name|artifact
parameter_list|)
block|{
name|this
operator|.
name|artifactId
operator|=
name|artifact
expr_stmt|;
block|}
comment|/**      * @return Returns the groupId.      */
specifier|public
name|String
name|getGroupId
parameter_list|()
block|{
return|return
name|groupId
return|;
block|}
comment|/**      * @param groupId The groupId to set.      */
specifier|public
name|void
name|setGroupId
parameter_list|(
name|String
name|groupId
parameter_list|)
block|{
name|this
operator|.
name|groupId
operator|=
name|groupId
expr_stmt|;
block|}
comment|/**      * @return Returns the type.      */
specifier|public
name|String
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
comment|/**      * @param type The type to set.      */
specifier|public
name|void
name|setType
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
comment|/**      * @return Returns the version.      */
specifier|public
name|String
name|getVersion
parameter_list|()
block|{
return|return
name|version
return|;
block|}
comment|/**      * @param version The version to set.      */
specifier|public
name|void
name|setVersion
parameter_list|(
name|String
name|version
parameter_list|)
block|{
name|this
operator|.
name|version
operator|=
name|version
expr_stmt|;
block|}
comment|/**      * @return Classifier.      */
specifier|public
name|String
name|getClassifier
parameter_list|()
block|{
return|return
name|classifier
return|;
block|}
comment|/**      * @param classifier Classifier.      */
specifier|public
name|void
name|setClassifier
parameter_list|(
name|String
name|classifier
parameter_list|)
block|{
name|this
operator|.
name|classifier
operator|=
name|classifier
expr_stmt|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|groupId
operator|+
literal|":"
operator|+
name|artifactId
operator|+
literal|":"
operator|+
name|classifier
operator|+
literal|":"
operator|+
name|version
operator|+
literal|":"
operator|+
name|type
return|;
block|}
comment|/**      * @return Returns the location.      */
specifier|public
name|File
name|getOutputDirectory
parameter_list|()
block|{
return|return
name|outputDirectory
return|;
block|}
comment|/**      * @param location The location to set.      */
specifier|public
name|void
name|setOutputDirectory
parameter_list|(
name|File
name|outputDirectory
parameter_list|)
block|{
name|this
operator|.
name|outputDirectory
operator|=
name|outputDirectory
expr_stmt|;
block|}
comment|/**      * @return Returns the location.      */
specifier|public
name|String
name|getDestFileName
parameter_list|()
block|{
return|return
name|destFileName
return|;
block|}
comment|/**      * @param destFileName The destFileName to set.      */
specifier|public
name|void
name|setDestFileName
parameter_list|(
name|String
name|destFileName
parameter_list|)
block|{
name|this
operator|.
name|destFileName
operator|=
name|destFileName
expr_stmt|;
block|}
block|}
end_class

end_unit

