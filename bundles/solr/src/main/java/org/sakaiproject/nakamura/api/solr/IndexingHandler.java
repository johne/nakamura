package org.sakaiproject.nakamura.api.solr;

import org.apache.solr.common.SolrInputDocument;
import org.osgi.service.event.Event;

import java.util.Collection;

/**
 * An index handler generates collections of documents to add and remove from the index
 * based on events. Index Handlers should explicitly register themselves against a Indexer
 * or TopicIndexer on activation and de-register on deactivation. Depending on the type of
 * IndexHandler and the registration the RepositorySession may adapt to one or many
 * repository sessions. If the adaptTo was registered with a TopicIndexer, then there
 * will be no guarantees that the RepositorySession will be adaptable to any type of
 * session. If the adaptTo was registered against a Indexer with a Session class
 * specification, then the RepositorySession will be adaptable to that type of repository
 * Session.
 */
public interface IndexingHandler {

  /**
   * Each Solr document that is to be indexed must set this field, so that the core indexing handlers can ensure that
   * the required fields are set. It will be removed before indexing takes place. 
   */
  public static final String _DOC_SOURCE_OBJECT = "_source";
  
  
  /**
   * The Solr Fields for the ID of the document, must be unique.
   */
  public static final String FIELD_ID = "id";

  /**
   * The Solr Field name for principals that can read, multivalued.
   */
  public static final String FIELD_READERS = "readers";


  /**
   * Standard, field, contains the Resource Type of the item.
   */
  public static final String FIELD_RESOURCE_TYPE = "resourceType";

  /**
   * The Field containing paths to the object.
   */
  public static final String FIELD_PATH = "path";

  /**
   * Get a Collection of documents to be added to the index based on the event.
   * 
   * @param repositorySession
   *          an adaptable RepositorySession.
   * @param event
   *          the event that specifies the list of documents.
   * @return a collection of SolrInputDocuments to be indexed.
   */
  Collection<SolrInputDocument> getDocuments(RepositorySession repositorySession,
      Event event);

  /**
   * Get a collection of queries to execute as delete operations against the index in
   * response to an event. Delete operations are performed before add operations.
   * 
   * @param respositorySession
   *          an adaptable RepositorySession.
   * @param event
   *          the event that specified the delete queries.
   * @return a Collection of lucene queries to be executed as Solr Delete operations.
   */
  Collection<String> getDeleteQueries(RepositorySession respositorySession, Event event);

}
