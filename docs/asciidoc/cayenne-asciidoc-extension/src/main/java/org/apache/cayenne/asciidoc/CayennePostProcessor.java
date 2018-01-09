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
name|asciidoc
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedWriter
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
name|nio
operator|.
name|file
operator|.
name|FileSystems
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|StandardOpenOption
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
name|asciidoctor
operator|.
name|Options
import|;
end_import

begin_import
import|import
name|org
operator|.
name|asciidoctor
operator|.
name|ast
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|asciidoctor
operator|.
name|ast
operator|.
name|DocumentRuby
import|;
end_import

begin_import
import|import
name|org
operator|.
name|asciidoctor
operator|.
name|extension
operator|.
name|Postprocessor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jsoup
operator|.
name|Jsoup
import|;
end_import

begin_comment
comment|/**  *<p>  * AsciidoctorJ post processor, that extracts ToC into separate file and optionally can inject content into rendered document.  * Can be used only for HTML backend, will<b>fail</b> if used with PDF.  *<p>  * It is targeted to inject "front-matter" section suitable for cayenne website tools.  *<p>  * Extension controlled by attributes in *.adoc file:  *<ul>  *<li>cayenne-header: header file name or constant "front-matter" that will inject empty front matter markup  *<li>cayenne-header-position [optional]: "top" to inject just above all content or "body" to inject right after&gt;body&lt; tag  *<li>cayenne-footer: footer file name or constant "front-matter" that will inject empty front matter markup  *<li>cayenne-footer-position [optional]: "bottom" to inject just after all content or "body" to inject right before&gt;/body&lt; tag  *</ul>  *  * @since 4.1  */
end_comment

begin_class
specifier|public
class|class
name|CayennePostProcessor
extends|extends
name|Postprocessor
block|{
specifier|private
specifier|static
specifier|final
name|String
name|FRONT_MATTER
init|=
literal|"front-matter"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|EMPTY_FRONT_MATTER
init|=
literal|"---\n---\n\n"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|POSITION_TOP
init|=
literal|"top"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|POSITION_BODY
init|=
literal|"body"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|POSITION_BOTTOM
init|=
literal|"bottom"
decl_stmt|;
specifier|public
name|CayennePostProcessor
parameter_list|(
name|DocumentRuby
name|documentRuby
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
block|}
specifier|public
name|String
name|process
parameter_list|(
name|Document
name|document
parameter_list|,
name|String
name|output
parameter_list|)
block|{
name|output
operator|=
name|extractTableOfContents
argument_list|(
name|document
argument_list|,
name|output
argument_list|)
expr_stmt|;
name|output
operator|=
name|fixupDom
argument_list|(
name|document
argument_list|,
name|output
argument_list|)
expr_stmt|;
name|output
operator|=
name|processHeader
argument_list|(
name|document
argument_list|,
name|output
argument_list|)
expr_stmt|;
name|output
operator|=
name|processFooter
argument_list|(
name|document
argument_list|,
name|output
argument_list|)
expr_stmt|;
return|return
name|output
return|;
block|}
specifier|private
name|String
name|fixupDom
parameter_list|(
name|Document
name|document
parameter_list|,
name|String
name|output
parameter_list|)
block|{
name|org
operator|.
name|jsoup
operator|.
name|nodes
operator|.
name|Document
name|jsoupDoc
init|=
name|Jsoup
operator|.
name|parseBodyFragment
argument_list|(
name|output
argument_list|)
decl_stmt|;
name|jsoupDoc
operator|.
name|select
argument_list|(
literal|".icon-note"
argument_list|)
operator|.
name|removeClass
argument_list|(
literal|"icon-note"
argument_list|)
operator|.
name|addClass
argument_list|(
literal|"fa-info-circle"
argument_list|)
operator|.
name|addClass
argument_list|(
literal|"fa-2x"
argument_list|)
expr_stmt|;
name|jsoupDoc
operator|.
name|select
argument_list|(
literal|".icon-tip"
argument_list|)
operator|.
name|removeClass
argument_list|(
literal|"icon-tip"
argument_list|)
operator|.
name|addClass
argument_list|(
literal|"fa-lightbulb-o"
argument_list|)
operator|.
name|addClass
argument_list|(
literal|"fa-2x"
argument_list|)
expr_stmt|;
name|jsoupDoc
operator|.
name|select
argument_list|(
literal|"code"
argument_list|)
operator|.
name|forEach
argument_list|(
name|el
lambda|->
block|{
name|String
name|codeClass
init|=
name|el
operator|.
name|attr
argument_list|(
literal|"data-lang"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|codeClass
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|el
operator|.
name|addClass
argument_list|(
name|codeClass
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|jsoupDoc
operator|.
name|select
argument_list|(
literal|"div#preamble"
argument_list|)
operator|.
name|remove
argument_list|()
expr_stmt|;
return|return
name|jsoupDoc
operator|.
name|body
argument_list|()
operator|.
name|html
argument_list|()
return|;
block|}
specifier|protected
name|String
name|extractTableOfContents
parameter_list|(
name|Document
name|document
parameter_list|,
name|String
name|output
parameter_list|)
block|{
name|int
name|start
init|=
name|output
operator|.
name|indexOf
argument_list|(
literal|"<div id=\"toc\" class=\"toc\">"
argument_list|)
decl_stmt|;
if|if
condition|(
name|start
operator|==
operator|-
literal|1
condition|)
block|{
comment|// no toc found, exit
return|return
name|output
return|;
block|}
name|String
name|tocEndString
init|=
literal|"</ul>\n</div>"
decl_stmt|;
name|int
name|end
init|=
name|output
operator|.
name|indexOf
argument_list|(
name|tocEndString
argument_list|,
name|start
argument_list|)
decl_stmt|;
if|if
condition|(
name|end
operator|==
operator|-
literal|1
condition|)
block|{
comment|// bad, no end..
return|return
name|output
return|;
block|}
name|end
operator|+=
name|tocEndString
operator|.
name|length
argument_list|()
operator|+
literal|1
expr_stmt|;
name|org
operator|.
name|jsoup
operator|.
name|nodes
operator|.
name|Document
name|tocDoc
init|=
name|Jsoup
operator|.
name|parseBodyFragment
argument_list|(
name|output
operator|.
name|substring
argument_list|(
name|start
argument_list|,
name|end
argument_list|)
argument_list|)
decl_stmt|;
name|tocDoc
operator|.
name|select
argument_list|(
literal|"ul"
argument_list|)
operator|.
name|addClass
argument_list|(
literal|"nav"
argument_list|)
expr_stmt|;
name|tocDoc
operator|.
name|select
argument_list|(
literal|"a"
argument_list|)
operator|.
name|addClass
argument_list|(
literal|"nav-link"
argument_list|)
expr_stmt|;
name|tocDoc
operator|.
name|select
argument_list|(
literal|"div#toc"
argument_list|)
operator|.
name|addClass
argument_list|(
literal|"toc-side"
argument_list|)
expr_stmt|;
name|String
name|toc
init|=
name|tocDoc
operator|.
name|body
argument_list|()
operator|.
name|html
argument_list|()
decl_stmt|;
name|Object
name|destDir
init|=
name|document
operator|.
name|getOptions
argument_list|()
operator|.
name|get
argument_list|(
name|Options
operator|.
name|DESTINATION_DIR
argument_list|)
decl_stmt|;
name|Object
name|docname
init|=
operator|(
operator|(
name|Map
operator|)
name|document
operator|.
name|getOptions
argument_list|()
operator|.
name|get
argument_list|(
name|Options
operator|.
name|ATTRIBUTES
argument_list|)
operator|)
operator|.
name|get
argument_list|(
literal|"docname"
argument_list|)
decl_stmt|;
name|Path
name|path
init|=
name|FileSystems
operator|.
name|getDefault
argument_list|()
operator|.
name|getPath
argument_list|(
operator|(
name|String
operator|)
name|destDir
argument_list|,
name|docname
operator|+
literal|".toc.html"
argument_list|)
decl_stmt|;
name|StandardOpenOption
index|[]
name|options
init|=
block|{
name|StandardOpenOption
operator|.
name|TRUNCATE_EXISTING
block|,
name|StandardOpenOption
operator|.
name|CREATE
block|,
name|StandardOpenOption
operator|.
name|WRITE
block|}
decl_stmt|;
try|try
init|(
name|BufferedWriter
name|br
init|=
name|Files
operator|.
name|newBufferedWriter
argument_list|(
name|path
argument_list|,
name|options
argument_list|)
init|)
block|{
name|br
operator|.
name|write
argument_list|(
name|toc
argument_list|,
literal|0
argument_list|,
name|toc
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|br
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|(
name|System
operator|.
name|err
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|start
operator|==
literal|0
condition|)
block|{
return|return
name|output
operator|.
name|substring
argument_list|(
name|end
argument_list|)
return|;
block|}
return|return
name|output
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|start
argument_list|)
operator|+
name|output
operator|.
name|substring
argument_list|(
name|end
argument_list|)
return|;
block|}
specifier|protected
name|String
name|processHeader
parameter_list|(
name|Document
name|document
parameter_list|,
name|String
name|output
parameter_list|)
block|{
name|String
name|headerFile
init|=
operator|(
name|String
operator|)
name|document
operator|.
name|getAttr
argument_list|(
literal|"cayenne-header"
argument_list|,
literal|""
argument_list|)
decl_stmt|;
name|String
name|headerPosition
init|=
operator|(
name|String
operator|)
name|document
operator|.
name|getAttr
argument_list|(
literal|"cayenne-header-position"
argument_list|,
name|POSITION_TOP
argument_list|)
decl_stmt|;
if|if
condition|(
name|headerFile
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|output
return|;
block|}
name|String
name|header
init|=
literal|""
decl_stmt|;
comment|// inject empty front matter
if|if
condition|(
name|FRONT_MATTER
operator|.
name|equals
argument_list|(
name|headerFile
operator|.
name|trim
argument_list|()
argument_list|)
condition|)
block|{
name|header
operator|=
name|EMPTY_FRONT_MATTER
expr_stmt|;
block|}
else|else
block|{
comment|// treat as a file
name|header
operator|=
name|document
operator|.
name|readAsset
argument_list|(
name|headerFile
argument_list|,
name|Collections
operator|.
name|emptyMap
argument_list|()
argument_list|)
expr_stmt|;
block|}
switch|switch
condition|(
name|headerPosition
operator|.
name|trim
argument_list|()
condition|)
block|{
case|case
name|POSITION_BODY
case|:
block|{
name|int
name|bodyStart
init|=
name|output
operator|.
name|indexOf
argument_list|(
literal|"<div id=\"header\">"
argument_list|)
decl_stmt|;
if|if
condition|(
name|bodyStart
operator|==
operator|-
literal|1
condition|)
block|{
comment|// no header
return|return
name|header
operator|+
name|output
return|;
block|}
return|return
name|output
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|bodyStart
argument_list|)
operator|+
name|header
operator|+
name|output
operator|.
name|substring
argument_list|(
name|bodyStart
argument_list|)
return|;
block|}
case|case
name|POSITION_TOP
case|:
default|default:
return|return
name|header
operator|+
name|output
return|;
block|}
block|}
specifier|protected
name|String
name|processFooter
parameter_list|(
name|Document
name|document
parameter_list|,
name|String
name|output
parameter_list|)
block|{
name|String
name|footerFile
init|=
operator|(
name|String
operator|)
name|document
operator|.
name|getAttr
argument_list|(
literal|"cayenne-footer"
argument_list|,
literal|""
argument_list|)
decl_stmt|;
name|String
name|footerPosition
init|=
operator|(
name|String
operator|)
name|document
operator|.
name|getAttr
argument_list|(
literal|"cayenne-footer-position"
argument_list|,
name|POSITION_BOTTOM
argument_list|)
decl_stmt|;
if|if
condition|(
name|footerFile
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|output
return|;
block|}
name|String
name|footer
init|=
name|document
operator|.
name|readAsset
argument_list|(
name|footerFile
argument_list|,
name|Collections
operator|.
name|emptyMap
argument_list|()
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|footerPosition
operator|.
name|trim
argument_list|()
condition|)
block|{
case|case
name|POSITION_BODY
case|:
block|{
name|int
name|bodyStart
init|=
name|output
operator|.
name|indexOf
argument_list|(
literal|"</body>"
argument_list|)
decl_stmt|;
if|if
condition|(
name|bodyStart
operator|==
operator|-
literal|1
condition|)
block|{
comment|// no footer
return|return
name|output
operator|+
name|footer
return|;
block|}
return|return
name|output
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|bodyStart
argument_list|)
operator|+
name|footer
operator|+
name|output
operator|.
name|substring
argument_list|(
name|bodyStart
argument_list|)
return|;
block|}
case|case
name|POSITION_BOTTOM
case|:
default|default:
return|return
name|output
operator|+
name|footer
return|;
block|}
block|}
block|}
end_class

end_unit

