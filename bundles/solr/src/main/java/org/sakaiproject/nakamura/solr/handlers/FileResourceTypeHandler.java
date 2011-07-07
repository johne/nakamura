package org.sakaiproject.nakamura.solr.handlers;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Reference;
import org.apache.solr.common.SolrInputDocument;
import org.osgi.service.event.Event;
import org.sakaiproject.nakamura.api.solr.RepositorySession;
import org.sakaiproject.nakamura.api.solr.ResourceIndexingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.RepositoryException;

@Component(immediate = true)
public class FileResourceTypeHandler extends DefaultResourceTypeHandler {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(FileResourceTypeHandler.class);
  @Reference(target="(type=jcr)")
  protected ResourceIndexingService resourceIndexingService;



  @Activate
  public void activate(Map<String, Object> properties) {
    resourceIndexingService.addHandler("nt:file", this);
    setResourceIndexingService(resourceIndexingService);
  }


  @Deactivate
  public void deactivate(Map<String, Object> properties) {
    resourceIndexingService.removeHandler("nt:file", this);
    setResourceIndexingService(null);
  }

  @Override
  public Collection<SolrInputDocument> getDocuments(RepositorySession repositorySession, Event event) {
    Collection<SolrInputDocument> docs = super.getDocuments(repositorySession, event);
    for (SolrInputDocument d : docs) {
      Node n = (Node) d.getFieldValue(_DOC_SOURCE_OBJECT);
      LOGGER.debug("Adding File Information to  {} ", n);
      try {
        if (n.hasNode(Node.JCR_CONTENT)) {
          Node content = n.getNode(Node.JCR_CONTENT);
          if (content != null) {
            LOGGER.debug("Loading Content");
            for (PropertyIterator pi = content.getProperties(); pi.hasNext();) {
              Property p = pi.nextProperty();
              String mappedName = index(p);
              if (mappedName != null) {
                for (Object o : convertToIndex(p)) {
                  LOGGER.debug("Storing {} as {} {}",new Object[]{p.getName(),mappedName,o});
                  d.addField(mappedName, o);
                }
              }
            }
          } else {
            LOGGER.debug("No Content Node found");
          }
        } else {
          LOGGER.debug("Content Node not present");
        }
      } catch (RepositoryException e) {
        LOGGER.error(e.getMessage(), e);
      }

    }
    return docs;
  }

  @Override
  public Collection<String> getDeleteQueries(RepositorySession repositorySession, Event event) {
    return super.getDeleteQueries(repositorySession, event);
  }

  public void setResourceIndexingService(ResourceIndexingService resourceIndexingService) {
    if (resourceIndexingService != null) {
      this.resourceIndexingService = resourceIndexingService;
    }
  }
}
