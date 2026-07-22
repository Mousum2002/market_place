package com.maketplace.analytics.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.maketplace.analytics.model.AnalyticsLogEntity;

public interface Repository extends JpaRepository<AnalyticsLogEntity, UUID> {

}
