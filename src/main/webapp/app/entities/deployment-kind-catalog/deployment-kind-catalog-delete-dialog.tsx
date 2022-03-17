import React, { useEffect, useState } from 'react';
import { RouteComponentProps } from 'react-router-dom';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntity, deleteEntity } from './deployment-kind-catalog.reducer';

export const DeploymentKindCatalogDeleteDialog = (props: RouteComponentProps<{ id: string }>) => {
  const [loadModal, setLoadModal] = useState(false);
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
    setLoadModal(true);
  }, []);

  const deploymentKindCatalogEntity = useAppSelector(state => state.deploymentKindCatalog.entity);
  const updateSuccess = useAppSelector(state => state.deploymentKindCatalog.updateSuccess);

  const handleClose = () => {
    props.history.push('/deployment-kind-catalog');
  };

  useEffect(() => {
    if (updateSuccess && loadModal) {
      handleClose();
      setLoadModal(false);
    }
  }, [updateSuccess]);

  const confirmDelete = () => {
    dispatch(deleteEntity(deploymentKindCatalogEntity.id));
  };

  return (
    <Modal isOpen toggle={handleClose}>
      <ModalHeader toggle={handleClose} data-cy="deploymentKindCatalogDeleteDialogHeading">
        Confirm delete operation
      </ModalHeader>
      <ModalBody id="releaseManagementApp.deploymentKindCatalog.delete.question">
        Are you sure you want to delete this DeploymentKindCatalog?
      </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp; Cancel
        </Button>
        <Button id="jhi-confirm-delete-deploymentKindCatalog" data-cy="entityConfirmDeleteButton" color="danger" onClick={confirmDelete}>
          <FontAwesomeIcon icon="trash" />
          &nbsp; Delete
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default DeploymentKindCatalogDeleteDialog;
