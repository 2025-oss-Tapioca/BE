package com.tapioca.BE.application.service.erd;


import com.tapioca.BE.adapter.out.mapper.ErdMapper;
import com.tapioca.BE.application.command.erd.UpdateDiagramsCommand;
import com.tapioca.BE.application.dto.response.erd.ErdResponseDto;
import com.tapioca.BE.config.exception.ErrorCode;
import com.tapioca.BE.domain.model.Attribute;
import com.tapioca.BE.domain.model.Diagram;
import com.tapioca.BE.domain.model.Erd;
import com.tapioca.BE.domain.model.type.AttributeType;
import com.tapioca.BE.domain.port.in.usecase.erd.ErdUseCase;
import com.tapioca.BE.domain.port.out.repository.erd.ErdRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ErdService implements ErdUseCase {
    private final ErdRepository erdRepository;
    private final ErdMapper erdMapper;

    @Override
    @Transactional
    public ErdResponseDto updateDiagrams(UpdateDiagramsCommand cmd) {
        Erd erd = erdRepository.findByTeamId(cmd.userId())
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_ERD.getMessage()));

        erd.getDiagrams().stream().map(Diagram::getId)
                .forEach(erd::removeDiagram);

        for (UpdateDiagramsCommand.DiagramInfo diagramInfo : cmd.getDiagramInfos()) {
            Diagram diagram = new Diagram(diagramInfo.diagramId(), diagramInfo.name());

            for (UpdateDiagramsCommand.AttributeInfo attributeInfo : diagramInfo.attributes()) {
                AttributeType type = AttributeType.valueOf(attributeInfo.getAttributeType());
                Attribute attr = new Attribute(
                        attributeInfo.attributeId(),
                        attributeInfo.name(),
                        type,
                        attributeInfo.length(),
                        attributeInfo.primaryKey(),
                        attributeInfo.foreignKey()
                );
                diagram.addAttribute(attr);
            }

            erd.addDiagram(diagram);
        }

        Erd saved = erdRepository.save(erd);
        return erdMapper.toResponse(saved);
    }
}
